#!/bin/bash
set -eux  # Exit on error and print all executed commands

# -----------------------------
# Install Java and Dependencies
# -----------------------------
sudo apt update -y
sudo apt install -y fontconfig openjdk-21-jre wget curl gnupg

# -----------------------------
# Install Jenkins
# -----------------------------
sudo mkdir -p /etc/apt/keyrings
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update -y
sudo apt install -y jenkins

# -----------------------------
# Jenkins Configuration
# -----------------------------
sudo mkdir -p /var/lib/jenkins/init.groovy.d

# Create an admin user and disable setup wizard
sudo tee /var/lib/jenkins/init.groovy.d/01-basic-security.groovy > /dev/null <<'EOF'
import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

println "--> Creating local admin user..."

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "admin")
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)

instance.save()
EOF

# # -----------------------------
# # Plugin Auto Installation
# # -----------------------------
# sudo tee /var/lib/jenkins/init.groovy.d/02-install-plugins.groovy > /dev/null <<'EOF'
# import jenkins.model.*
# import hudson.PluginManager

# def instance = Jenkins.getInstance()
# def pm = instance.getPluginManager()
# def uc = instance.getUpdateCenter()

# def plugins = [
#   "ssh-agent",
# ]

# println "--> Installing default plugins..."
# plugins.each { pluginName ->
#     if (!pm.getPlugin(pluginName)) {
#         println "Installing plugin: ${pluginName}"
#         def plugin = uc.getPlugin(pluginName)
#         if (plugin) {
#             plugin.deploy()
#         } else {
#             println "Warning: Plugin ${pluginName} not found in Update Center"
#         }
#     } else {
#         println "Plugin ${pluginName} already installed."
#     }
# }

# instance.save()
# EOF

# -----------------------------
# Start Jenkins
# -----------------------------
sudo systemctl enable jenkins
sudo systemctl restart jenkins

echo "------------------------------------------------------"
echo " Jenkins installation complete!"
echo " URL:    http://<your-ec2-public-ip>:8080"
echo " User:   admin"
echo " Pass:   admin"
echo "------------------------------------------------------"
