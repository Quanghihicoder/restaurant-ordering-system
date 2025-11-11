terraform {

  backend "s3" {
    bucket         = "qfood-terraform"
    key            = "terraform/terraform.tfstate"
    region         = "ap-southeast-2"
    dynamodb_table = "qfood-terraform-lock"
    encrypt        = true
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Use the default VPC
data "aws_vpc" "default" {
  default = true
}

# Pick a subnet from the default VPC
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Latest Ubuntu 22.04 LTS (Jammy) AMI from Canonical
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# EC2 security groups
resource "aws_security_group" "ec2" {
  name        = "qfood-jenkins-sg"
  description = "Allow SSH and HTTP"
  vpc_id      = data.aws_vpc.default.id

  ingress = [
    for port in [22, 80, 443, 8080] : {
      from_port   = port
      to_port     = port
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }]


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "qfood-jenkins-sg"
  }
}

# Jenkins Server
resource "aws_instance" "jenkins" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t2.micro"
  subnet_id                   = data.aws_subnets.default.ids[0]
  associate_public_ip_address = true
  vpc_security_group_ids      = [aws_security_group.ec2.id]
  key_name                    = "personal"

  # Use the file directly (simple)…
  user_data = file("${path.module}/jenkins_user_data.sh")

  user_data_replace_on_change = true

  tags = {
    Name = "Jenkins-Server"
  }
}

# Login with admin, admin | Click install recommend plugins | Skip first admin user 
# Manually install 
# SSH Agent, Eclipse Temurin, SonarQube Scanner, Sonar Quality Gates, Quality Gates 
# Nodejs, Docker, Docker commons, Docker pipeline, Docker API, docker build step, CloudBees Docker Build and Publish
# Plugins
# Restart
# In tools, add nodejs, add jdk, add docker, add sonarqube scanner
output "jenkins_server_public_url" {
  description = "Access Jenkins Server"
  value       = "http://${aws_instance.jenkins.public_ip}:8080"
}
