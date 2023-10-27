job("Distribute Core Domain Packages") {
  
	startOn {
        gitPush {
            anyBranchMatching {
                +"release/*"
                +"master"
            }
        }
    }

    // check out eapp-python-domain to /mnt/space/work/eapp-python-domain
    git("eapp-python-domain")

	parameters {
      text("EAPP_PROTO_SRC_DIR", value = "/mnt/space/work/eapp-service-core/src/main/proto")
      text("EAPP_PROTO_PYTHON_OUT_DIR", value = "/mnt/space/work/eapp-python-domain/src/eapp_python_domain")
      text("EAPP_PROTO_NODEJS_OUT_DIR", value = "/mnt/space/work/eapp-nodejs-domain/eapp-nodejs-domain")

      text("EAPP_CORE_DOMAIN_DIR", value = "/mnt/space/work/eapp-service-core")
      text("EAPP_PYTHON_DOMAIN_DIR", value = "/mnt/space/work/eapp-python-domain")
    }

    container(displayName = "Configure Source Directories", image = "amazoncorretto:17-alpine") {

        env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
        env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
      
        kotlinScript { api ->

            val EAPP_PROTO_SRC_DIR = System.getenv("EAPP_PROTO_SRC_DIR")
            val EAPP_PROTO_PYTHON_OUT_DIR = System.getenv("EAPP_PROTO_PYTHON_OUT_DIR")
            
            val PROTO_INCLUDE_DIRS = """
                ${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/entities/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/galaxy/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/machine/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/organisation/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/space/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/action/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page_para/*.proto,
            """.trimIndent()
            
            var PROTO_INCLUDES = ""
            PROTO_INCLUDE_DIRS.split(",").forEach {
                var temp = it.trim()
                temp = temp.trim()
                PROTO_INCLUDES = "$PROTO_INCLUDES $temp"
            }

            api.parameters["PROTO_INCLUDES"] = PROTO_INCLUDES
        }
    }

    parallel {

        sequential {
            
            container(displayName = "Python Domain Build", image = "python:3.9.16") {

                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """
                  
                  	echo "Ensure you can run pip from the command line"
                  	python3 -m pip --version
                   	python3 -m ensurepip --default-pip

                    echo "Ensure pip, setuptools, and wheel are up to date"
                    python3 -m pip install --upgrade pip setuptools wheel

                    echo "Install required packages for python build"
                    python3 -m pip install protobuf==3.14.0
                    python3 -m pip install grpcio-tools==1.34.0
                    python3 -m pip install grpcio==1.34.0
                    python3 -m pip install twine==4.0.1

                   	# FOR PYTHON DOMAIN
                   	rm -rf ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos
                   	rm -rf ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/gramx
                   	
                    echo "Build the python domain proto client codes"
                    echo ${'$'}PROTO_INCLUDES
                    python3 -m grpc_tools.protoc \
                      --python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      --grpc_python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES

                    find ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos -type d -exec touch {}/__init__.py \;

                    echo "Change directory to eapp-python-domain"
                    cd /mnt/space/work/eapp-python-domain

                    echo "Configure pypirc"
                    cp /mnt/space/work/eapp-python-domain/pypirc ~/.pypirc

                    echo "Setup Updated Version"
                    CURRENT_YEAR=$(date +'%y')
                    CURRENT_MONTH=$(date +'%m')
                    VERSION_NUMBER=${"$"}CURRENT_YEAR.${"$"}CURRENT_MONTH.${"$"}JB_SPACE_EXECUTION_NUMBER
                    echo ${"$"}VERSION_NUMBER

                    sed "10s/.*/    version='${"$"}CURRENT_YEAR.${"$"}CURRENT_MONTH.${"$"}JB_SPACE_EXECUTION_NUMBER',/" /mnt/space/work/eapp-python-domain/setup.py > /mnt/space/work/eapp-python-domain/newsetup.py
                    mv /mnt/space/work/eapp-python-domain/newsetup.py /mnt/space/work/eapp-python-domain/setup.py

                    echo "Build Package"
                    python3 setup.py sdist

                    echo "Inspect Package"
                    tar -tvf /mnt/space/work/eapp-python-domain/dist/ethos-${"$"}VERSION_NUMBER.tar.gz

                    echo "Publish Package"
                    twine upload -r local dist/*
                    
                  """
                }
            }
        }	 // end of python domain sequential build

        sequential {
            container(displayName = "Nodejs Domain Build", image = "node") {
                shellScript {
                  content = """
                    pwd
                    ls -l
                    echo "{{ PROTO_INCLUDES }}"
                  """
                }
            }
        }	// end of nodejs domain sequential build
        
    }	// end of all domain parallel build

    parallel {
        sequential {
            container(displayName = "Run Python Domain Capability Contract Behaviour Tests", image = "python:3.9.16") {

                // load up all required paths in environments
                env["EAPP_CORE_DOMAIN_DIR"] = "{{ EAPP_CORE_DOMAIN_DIR }}"
                env["EAPP_PYTHON_DOMAIN_DIR"] = "{{ EAPP_PYTHON_DOMAIN_DIR }}"

                // start execution
                shellScript {
                  content = """

                    echo "install the environment dependencies"
                    pip install -r ${'$'}EAPP_PYTHON_DOMAIN_DIR/requirements.txt
                    # end of execution installing the environment dependencies

                    echo "start tests"
                    export PYTHONPATH="${'$'}EAPP_PYTHON_DOMAIN_DIR/src/tests/ethos/elint/services/product/identity/account/access_account/steps:${'$'}PYTHONPATH"
                    behave ${'$'}EAPP_CORE_DOMAIN_DIR/src/main/features/ethos/elint/services/product/identity/account/access_account/validateAccount.feature
                    # end of running tests

                  """
                }   // end of execution

            }   // end of running Python Domain Capability Contract Behaviour Tests
        }	// end of python domain implemented capability contract behaviour acceptance
    }	// end of all domain parallel build
}