# EAPP System Contracts 🏗️

[![CI/CD Pipeline](https://github.com/50gramx/eapp-system-contracts/workflows/Protobuf%20Distribution/badge.svg)](https://github.com/50gramx/eapp-system-contracts/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Protobuf](https://img.shields.io/badge/protobuf-v3.21+-green.svg)](https://developers.google.com/protocol-buffers)
[![Multi-Language](https://img.shields.io/badge/languages-Python%20%7C%20Dart%20%7C%20Node.js%20%7C%20Kotlin-orange.svg)](https://github.com/50gramx/eapp-system-contracts)

> **The Foundation of EAPP (Ethos Apps Platform)** - Core service contracts and protobuf definitions for polyglot development across multiple languages.

## 📋 Table of Contents

- [Overview](#-overview)
- [🚀 Quick Start](#-quick-start)
- [🏗️ Architecture](#️-architecture)
- [📦 Multi-Language Packages](#-multi-language-packages)
- [🔧 Development](#-development)
- [📚 Documentation](#-documentation)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

## 🌟 Overview

EAPP System Contracts serves as the **single source of truth** for all service definitions in the Ethos Apps Platform. This repository contains:

- **📄 Protocol Buffer Definitions** - Language-agnostic service contracts
- **🔗 Multi-Language Client Generation** - Automated code generation for Python, Dart, Node.js, and Kotlin
- **📦 Package Distribution** - Automated publishing to language-specific package repositories
- **🧪 BDD Specifications** - Behavior-driven development feature files

### 🎯 Key Features

- **🔄 Automated CI/CD Pipeline** - Protobuf compilation and package distribution
- **🌐 Polyglot Support** - Generate client code for multiple programming languages
- **📊 Central Package Index** - Unified view of all language packages
- **🔒 Private Package Hosting** - Secure distribution via GitHub Releases
- **⚡ Parallel Processing** - Fast, concurrent language compilation

## 🚀 Quick Start

### 1. View Package Index
Visit our **central package index** to see all available language packages:
```
https://html-preview.github.io/?url=https://raw.githubusercontent.com/50gramx/eapp-system-contracts/master/packages/index.html
```

### 2. Install Language-Specific Packages

#### 🐍 Python
```bash
pip install --index-url https://raw.githubusercontent.com/50gramx/eapp-python-domain/master/packages/index.html eapp-python-domain
```

#### 🎯 Dart
```yaml
# pubspec.yaml
dependencies:
  eapp_dart_domain: ^0.1.0
```

#### 🟨 Node.js
```bash
npm install eapp-nodejs-domain
```

#### ☕ Kotlin/Java
```kotlin
// build.gradle.kts
implementation("com.ethosverse:eapp-kotlin-domain:0.1.0")
```

### 3. Use in Your Code

```python
# Python Example
from eapp_python_domain.ethos import user_pb2

user = user_pb2.User()
user.id = "user123"
user.name = "John Doe"
```

```dart
// Dart Example
import 'package:eapp_dart_domain/ethos/user.pb.dart';

final user = User()
  ..id = 'user123'
  ..name = 'John Doe';
```

## 🏗️ Architecture

### Repository Structure
```
eapp-system-contracts/
├── 📁 src/main/proto/          # Protocol Buffer definitions
│   ├── ethos/                  # Core EAPP entities
│   ├── services/               # Service definitions
│   └── features/               # BDD feature files
├── 📁 .github/workflows/       # CI/CD pipelines
│   └── protobuf-distribution.yml
├── 📁 packages/                # Central package index
│   └── index.html              # Beautiful HTML index
└── 📁 docs/                    # Documentation
```

### Service Categories

| Category | Description | Examples |
|----------|-------------|----------|
| **🔐 Identity** | User authentication & authorization | `AccountService`, `AuthService` |
| **💬 Communication** | Messaging & notifications | `ConversationService`, `NotificationService` |
| **🧠 Cognitive** | AI & knowledge management | `KnowledgeService`, `ContextService` |
| **🛍️ Commerce** | Transactions & payments | `PurchaseService`, `PaymentService` |
| **🌌 Multiverse** | Space & universe management | `SpaceService`, `UniverseService` |

## 📦 Multi-Language Packages

Our automated pipeline generates and publishes packages for multiple languages:

| Language | Repository | Package Index | Latest Release |
|----------|------------|---------------|----------------|
| 🐍 **Python** | [eapp-python-domain](https://github.com/50gramx/eapp-python-domain) | [View Packages](https://raw.githubusercontent.com/50gramx/eapp-python-domain/master/packages/index.html) | ![Python Release](https://img.shields.io/github/v/release/50gramx/eapp-python-domain) |
| 🎯 **Dart** | [eapp-dart-domain](https://github.com/50gramx/eapp-dart-domain) | [View Releases](https://github.com/50gramx/eapp-dart-domain/releases) | ![Dart Release](https://img.shields.io/github/v/release/50gramx/eapp-dart-domain) |
| 🟨 **Node.js** | [eapp-nodejs-domain](https://github.com/50gramx/eapp-nodejs-domain) | [View Releases](https://github.com/50gramx/eapp-nodejs-domain/releases) | ![Node.js Release](https://img.shields.io/github/v/release/50gramx/eapp-nodejs-domain) |
| ☕ **Kotlin** | [eapp-kotlin-domain](https://github.com/50gramx/eapp-kotlin-domain) | [View Releases](https://github.com/50gramx/eapp-kotlin-domain/releases) | ![Kotlin Release](https://img.shields.io/github/v/release/50gramx/eapp-kotlin-domain) |

### 🎨 Central Package Index
Visit our beautiful, interactive package index:
```
https://html-preview.github.io/?url=https://raw.githubusercontent.com/50gramx/eapp-system-contracts/master/packages/index.html
```

## 🔧 Development

### Prerequisites
- **Protocol Buffers** v25.1+
- **Python** 3.9+ (for Python package generation)
- **Node.js** 18+ (for Node.js package generation)
- **Java** 11+ (for Kotlin package generation)
- **Dart** 3.0+ (for Dart package generation)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/50gramx/eapp-system-contracts.git
   cd eapp-system-contracts
   ```

2. **Install protoc compiler**
   ```bash
   # Download protoc
   wget https://github.com/protocolbuffers/protobuf/releases/download/v25.1/protoc-25.1-linux-x86_64.zip
   unzip protoc-25.1-linux-x86_64.zip -d $HOME/protoc
   export PATH="$HOME/protoc/bin:$PATH"
   ```

3. **Compile protobuf files**
   ```bash
   # Python
   protoc --python_out=./generated/python --proto_path=src/main/proto src/main/proto/**/*.proto
   
   # Dart
   protoc --dart_out=./generated/dart --proto_path=src/main/proto src/main/proto/**/*.proto
   
   # Node.js
   protoc --js_out=import_style=commonjs,binary:./generated/nodejs --proto_path=src/main/proto src/main/proto/**/*.proto
   
   # Kotlin/Java
   protoc --java_out=./generated/kotlin --proto_path=src/main/proto src/main/proto/**/*.proto
   ```

### CI/CD Pipeline

Our automated pipeline triggers on:
- **Push to master** with changes in `src/main/proto/**`
- **Manual workflow dispatch**

**Pipeline Steps:**
1. **Parallel Language Compilation** - All languages compile simultaneously
2. **Package Building** - Create language-specific packages
3. **Release Creation** - Create GitHub releases with unique versions
4. **Asset Upload** - Upload package files to releases
5. **Index Generation** - Update package indexes
6. **Central Index Update** - Update master package index

## 📚 Documentation

### API Reference
- **Protocol Buffer Definitions**: [src/main/proto/](src/main/proto/)
- **Service Documentation**: [docs/services/](docs/services/)
- **BDD Specifications**: [src/main/features/](src/main/features/)

### Examples
- **Python Examples**: [examples/python/](examples/python/)
- **Dart Examples**: [examples/dart/](examples/dart/)
- **Node.js Examples**: [examples/nodejs/](examples/nodejs/)
- **Kotlin Examples**: [examples/kotlin/](examples/kotlin/)

### Guides
- [Getting Started Guide](docs/getting-started.md)
- [Contributing Guidelines](CONTRIBUTING.md)
- [API Design Principles](docs/api-design.md)

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Workflow
1. **Fork** the repository
2. **Create** a feature branch
3. **Add** your protobuf definitions
4. **Test** with multiple languages
5. **Submit** a pull request

### Code Standards
- **Protocol Buffers**: Follow [Google's Style Guide](https://developers.google.com/protocol-buffers/docs/style)
- **Documentation**: Include comprehensive comments
- **Testing**: Add BDD feature files for new services

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🔗 Quick Links

- **🌐 Website**: [https://50gramx.com](https://50gramx.com)
- **📧 Support**: [support@50gramx.com](mailto:support@50gramx.com)
- **🐛 Issues**: [GitHub Issues](https://github.com/50gramx/eapp-system-contracts/issues)
- **💬 Discussions**: [GitHub Discussions](https://github.com/50gramx/eapp-system-contracts/discussions)
- **📖 Wiki**: [GitHub Wiki](https://github.com/50gramx/eapp-system-contracts/wiki)

---

<div align="center">
  <p><strong>Built with ❤️ by the EAPP Team</strong></p>
  <p><em>Empowering polyglot development across the Ethos Apps Platform</em></p>
</div>
# Test workflow trigger
