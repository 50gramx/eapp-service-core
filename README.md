# Ethos Apps Service Core (`eapp-service-core`)

Welcome to the Ethos Apps Service Core (`eapp-service-core`) repository. This repository contains the core service contracts essential for the proper functioning of the Ethos Apps system. The contracts are defined in a way to encourage polyglot development and support implementations across multiple languages.

## Overview

The Ethos Apps system aims to provide a scalable and extensible platform. The `eapp-service-core` repository acts as the foundation, holding all the core service contracts, which are not defined by end-users or collaborators but are integral to the system.

- **Polyglot Nature**: These contracts are designed with a language-agnostic approach, allowing implementations in various languages.
- **Modular Design**: Each service contract (RPC) is seen as a distinct capability of the system. This modular approach ensures clarity, scalability, and promotes independent development and testing.
- **Behavior-Driven Development (BDD)**: Alongside the service contracts, this repository hosts the Gherkin feature files for BDD, enabling clear specification of expected behaviors and promoting collaboration between developers, testers, and non-technical stakeholders.

## Directory Structure

A glimpse of our repository's architecture:

eapp-service-core/

|-- proto-files/ # Contains the .proto files with service definitions

|-- features/ # Contains the Gherkin feature files for BDD

|-- docs/ # Documentation related to service contracts

`-- ... # Other directories as per the requirement

## Services


A concise list of core services within this repository:

- **Elint Entities**: Contracts defining various core entities within the system, such as accounts, spaces, galaxies, etc.
- **Cognitive Services**: Focus on context and knowledge-related operations.
- **Product Services**: Encompasses action, conversation, identity, and knowledge-related operations.

[... For brevity, detailed services are not listed. Refer to the respective folders for specific RPCs and their definitions ...]



## CI/CD Pipeline and Distribution

This repository is equipped with a CI/CD pipeline that automates the process of client code generation based on the gRPC contracts. Here's how it works:

1. **Client Code Generation**: Upon any new commit, the CI/CD pipeline triggers a process to generate language-specific client codes. These codes are then pushed to language-specific repositories, like `eapp-python-domain`.
2. **Packaging and Distribution for Python**: For repositories such as `eapp-python-domain`, the CI/CD setup further automates the process of packaging the generated client codes into a Python package. This package is subsequently pushed to our private Python index.
3. **Usage in Other Repositories**: Services and microservices, when developed using Python, can directly include the Python package from the private Python index as a dependency. This ensures they always stay updated with the latest contracts without manual intervention or configuration.

### Benefits:

- **Consistency**: Ensures all services and clients are consistent with the latest gRPC contracts.
- **Reduction in Manual Effort**: Eliminates the need for manual code generation, packaging, and distribution.
- **Rapid Development**: Developers can quickly use the latest contracts without waiting for manual updates.



## Contributing

If you wish to contribute to the `eapp-service-core`, please follow the [CONTRIBUTING.md](./CONTRIBUTING.md) guidelines.

## Usage

To use these contracts in your project:

1. Clone the repository: `git clone https://github.com/[your-organization]/eapp-service-core.git`
2. [Instructions to generate language-specific stubs from .proto files, if required]
3. Integrate the generated stubs or the feature files in your implementation or testing framework.

## Future Scope

- **User/Collaborator Defined Services**: In the future, the Ethos Apps system is set to host services defined by users or collaborators, widening its capabilities and offering more versatility.

## Support & Feedback

For any queries, issues, or feedback related to `eapp-service-core`, please [create an issue](https://github.com/50gramx/eapp-service-core/issues) or contact [support@50gramx.com].
