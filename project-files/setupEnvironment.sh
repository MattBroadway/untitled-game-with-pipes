#!/bin/bash

# run this to set up the environment in an Ubuntu installation
# without java already installed

sudo apt-get update
sudo apt-get upgrade
sudo apt-get install default-jre default-jdk

bash ./getLibs.sh
