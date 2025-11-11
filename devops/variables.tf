#  Region 
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-southeast-2"
}
variable "region_id" {
  description = "ID of the AWS Region"
  type        = string
  default     = "783225319266"
}

variable "aza" {
  description = "Availability Zone A of the AWS region"
  type        = string
  default     = "ap-southeast-2a"
}

variable "azb" {
  description = "Availability Zone B of the AWS region"
  type        = string
  default     = "ap-southeast-2b"
}


