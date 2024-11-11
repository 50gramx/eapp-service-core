
---

# Service Collars - Podeage Uno Template

This guide outlines the steps to reserve and document service collars for various single-container pods within the Ethos Pods organization. Each collar allows creators to establish new applications for customers, which can then be reserved and managed through the collar documentation.

By following this README, creators with access can help grow and maintain the collar documentation, supporting faster development for both current and future team members.

---

## Steps to Build Service Collars

### 1. Draft the `DCX(-1) Collar.proto`

- **Objective**: Set up a new collar based on the Podeage Uno template for single-container pod applications.
- **Instructions**:
  - Navigate to the **50gramx** Git repository in the following path:  
    ```
    eapp-service-core/src/main/proto/ethos/elint/collars
    ```
  - Copy the existing collar template `.proto` file and rename it to `DCX(-1) Collar.proto`.
  - Ensure the file follows naming conventions and structure similar to the original template.

### 2. Draft the Required Collar Entities

- **Objective**: Define the key entities that will represent the collar for the specific pod application.
- **Instructions**:
  - Identify the entities necessary for the new service collar based on the pod's functionalities (e.g., Ubuntu, Jupyter Notebook).
  - Draft the entities in the newly created `DCX(-1) Collar.proto` file, following existing syntax and structure from the template.

### 3. Draft the Collar Entity RPCs

- **Objective**: Define Remote Procedure Calls (RPCs) for the collar entities.
- **Instructions**:
  - In the same `DCX(-1) Collar.proto` file, draft the RPCs required to interact with the collar entities.
  - Each RPC should be designed to support the unique functions of the pod application.
  - Ensure consistency with other collar RPCs by referencing similar entities in the template.

### 4. Review and Finalize Collar Entities and Protos

- **Objective**: Ensure that the new collar entities and RPCs are ready to be merged into the master branch.
- **Instructions**:
  - Conduct a thorough review of the collar entities and RPCs to confirm accuracy and completeness.
  - Make necessary adjustments based on feedback and align with the Podeage Uno template standards.
  - Once finalized, prepare the `DCX(-1) Collar.proto` for a merge request to the master branch.

---

## Implement Collar Entity Contextual Capabilities

Now, we focus on implementing the contextual capabilities for the collar entities, ensuring that each service works as intended in Python.

### 1. Import and Update Proto Client Code for Collar Entities

This step ensures that the latest Python gRPC client code for the collar entities and services is integrated into your project. This will allow your implementation of the collar entities to work seamlessly with the backend services. Follow the instructions carefully to update the required dependencies and files.

#### **Sub-Step 1: Check the Latest Version Published by CI/CD for Python Collar Contracts**

1. **Objective**: Ensure the latest Python client code for the collar contracts has been successfully published via CI/CD.
2. **Instructions**:
   - Navigate to the CI/CD pipeline results and check the latest version of the Python collar package.
   - Visit the JetBrains Spaces repository for the **Ethos** package version:
     - [Ethos Package Version (JetBrains Spaces)](https://50gramx.jetbrains.space/p/main/packages/pypi/python-delivery/ethos?v=24.11.196)
   - Confirm the version number in the package matches the latest release from the CI/CD pipeline.

#### **Sub-Step 2: Update the `requirements.txt` in `eapp-python-implementation`**

1. **Objective**: Update the `requirements.txt` file to ensure the correct package version is installed.
2. **Instructions**:
   - Open the `requirements.txt` file in the **`eapp-python-implementation`** project directory.
     - Path: `eapp-python-implementation/requirements.txt`
   - Update the **`ethos`** package version to match the latest release (e.g., `ethos>=24.11.196`).
     ```txt
     ethos>=24.11.196
     ```
   - Save the changes to the `requirements.txt` file.

#### **Sub-Step 3: Install the Updated Package Locally**

1. **Objective**: Ensure the latest version of the **Ethos Core** and **Layer 1 contracts** are pulled into your local environment.
2. **Instructions**:
   - Run the following command to install the dependencies from `requirements.txt`, including the latest **Ethos** version.
     ```bash
     pip3 install -r requirements.txt --extra-index-url https://85301fb9-f857-4cbd-b919-d030bf6423ce:50e72eeaf9363ba1d9ec14dcfa3b08814dd4b0935c91b50756efd839b2e64c66@pypi.pkg.jetbrains.space/50gramx/p/main/python-delivery/simple
     ```
   - This command ensures the latest version of **Ethos Core** and **Layer 1 Contracts** is pulled and installed locally.

3. **Verify Installation**:
   - After running the command, ensure that the installation process completes without errors.
   - If any issues arise during the installation (e.g., dependency conflicts or network issues), address and resolve them before proceeding.

#### **Sub-Step 4: Copy Collar Folder and Paste in Correct Directory**

1. **Objective**: Prepare the collar directory for the new collar entity by copying the necessary folder.
2. **Instructions**:
   - Navigate to the following directory in the project:
     - `eapp-python-implementation/src/community/gramx/collars/DC499999998`
   - Copy the folder **`DC499999998`** and paste it into the `collars` directory.
     - Ensure you name the folder appropriately according to the collar entity you're implementing.
   - This will be the base folder for the new collar entity, and you will update the code within this folder based on the requirements of the collar.

#### Sub-Step 5: Update Files for the Collar Entity 

1. **Objective**: Update the files for the newly copied collar entity folder.
2. **Instructions**:
   - In the new collar folder, you need to update:
     - **4 basic files** for the collar.
     - **2 additional files** specific to your collar entity.
   - Modify these files to match the new collar requirements based on `DCX(-1) Collar.proto`.
3. **Basic Implementations**:
    - <details>
        <summary>Implementing `model.py` for Collar Entity</summary>

        ###### 1. **Rename the Collar**
        - **Objective**: Change the collar name to match the new one.
        - **Action**: 
          - Replace old collar names (e.g., `DC499999998`) with the new collar name everywhere in the model.

        ###### 2. **Replace Collar Entities**
        - **Objective**: Swap old entities with the new ones.
        - **Action**:
          - Replace placeholder entities like `VMInstance`, `UsageMetric`, `Alert` with the new collar's entities.

        ###### 3. **Update Foreign Key Relationships**
        - **Objective**: Ensure foreign keys point to the correct tables.
        - **Action**:
          - Update `VMInstance`, `UsageMetric`, and `Alert` to reference the right table names based on the new collar.

        ###### 4. **Adjust Model and Table Names**
        - **Objective**: Make sure model and table names are consistent.
        - **Action**:
          - Update table names (`__tablename__`) and model names to match the new collar naming.

        ###### 5. **Integrate with `ServiceSpaceModelBase`**
        - **Objective**: Make sure models inherit from `ServiceSpaceModelBase`.
        - **Action**:
          - Verify that `VMInstance`, `UsageMetric`, and `Alert` extend `ServiceSpaceModelBase`.

        ###### 6. **Ensure Unique Model Names**
        - **Objective**: Ensure each model name is unique.
        - **Action**:
          - Check model constructors to make sure names like `vm_instance_model_name`, `usage_metric_model_name`, etc., are unique.

        ###### 7. **Add Necessary Columns for the New Collar**
        - **Objective**: Update or add columns needed for the collar.
        - **Action**:
          - Add or modify columns (e.g., `name`, `status`, `cpu_cores`) based on the new collar's needs.

        ###### 8: **Update service_space_models.py for Collar Setup**
        - **Objective**: Update the add_new_domain function to include the new collar's setup.
        - **Instructions**:
          - After creating the model python file for the new collar, update the service_space_models.py to ensure the collar tables are set up correctly by adding a check for the new collar name and a function call for its setup.

      </details>
