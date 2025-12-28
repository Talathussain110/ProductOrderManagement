# Product Order Management System

A Spring Boot web application for managing products and orders, developed with an emphasis on clean architecture, test-driven development, and high code quality enforced by SonarCloud.  
The application provides It provides functionalities for adding, updating, and retrieving product or order information while ensuring secure and scalable operations.

---

## Sonar Cloud and CoverAlls Coverage Badge:
Sonarcloud: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Talathussain110_ProductOrderManagement&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Talathussain110_ProductOrderManagement)

Code Coverall: [![Coverage Status](https://coveralls.io/repos/github/Talathussain110/ProductOrderManagement/badge.svg?branch=master)](https://coveralls.io/github/Talathussain110/ProductOrderManagement?branch=master)

## 📌 Project Overview

This project enables users to:

- **Manage products:** create, update, view, delete
- **Create and manage orders** associated with products
- **Access features via:**
  - RESTful APIs
  - Web UI built with Thymeleaf
- **Maintain high software quality through:**
  - Automated tests
  - Code analysis
  - Full branch coverage

---

## Features

- **Java 17**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA: Integration with MySQL database.**
- **Thymeleaf: UI rendering for web application views**
- **MySQL (Dockerized) / H2 (for tests)**
- **Selenium: E2E testing.**
- **Jacoco & Pitest (Code coverage and mutation testing)**
- **SonarCloud (static analysis & quality gate)**
- **TestContainers: Containerized testing support**

---

## Setup and Installation

### Prerequisites
- Java 17 or later  
- Maven  
- Docker (for MySQL database)  

### Steps
1. Clone the repository:  
   ```bash
   git clone <[repository_url](https://github.com/Talathussain110/ProductOrderManagement>
   cd ProductOrderManagement
