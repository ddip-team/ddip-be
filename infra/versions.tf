terraform {
  required_version = ">= 1.6"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket  = "ddip-terraform-state"
    key     = "terraform/terraform.tfstate"
    region  = "ap-northeast-2"
    encrypt = true
    # dynamodb_table = "terraform-lock"
  }
}
