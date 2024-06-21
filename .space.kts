import java.time.LocalDate

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
    // check out eapp-nodejs-domain to /mnt/space/work/eapp-nodejs-domain
    git("eapp-nodejs-domain")
    // check out eapp-dart-domain to /mnt/space/work/eapp-dart-domain
    git("eapp-dart-domain")
    // check out eapp-kotlin-domain to /mnt/space/work/eapp-kotlin-domain
    git("eapp-kotlin-domain")
    // check out eapp-swift-domain to /mnt/space/work/eapp-swift-domain
    git("eapp-swift-domain")

	parameters {
      text("EAPP_PROTO_SRC_DIR", value = "/mnt/space/work/eapp-service-core/src/main/proto")
      text("EAPP_PROTO_PYTHON_OUT_DIR", value = "/mnt/space/work/eapp-python-domain/src/eapp_python_domain")
      text("EAPP_PROTO_NODEJS_OUT_DIR", value = "/mnt/space/work/eapp-nodejs-domain/eapp-nodejs-domain")
      text("EAPP_PROTO_DART_OUT_DIR", value = "/mnt/space/work/eapp-dart-domain/src/eapp_dart_domain")
      text("EAPP_PROTO_KOTLIN_OUT_DIR", value = "/mnt/space/work/eapp-kotlin-domain/eapp-nodejs-domain")
      text("EAPP_PROTO_SWIFT_OUT_DIR", value = "/mnt/space/work/eapp-swift-domain/eapp-nodejs-domain")

      text("EAPP_CORE_DOMAIN_DIR", value = "/mnt/space/work/eapp-service-core")
      text("EAPP_PYTHON_DOMAIN_DIR", value = "/mnt/space/work/eapp-python-domain")
      text("EAPP_NODEJS_DOMAIN_DIR", value = "/mnt/space/work/eapp-nodejs-domain")
      text("EAPP_DART_DOMAIN_DIR", value = "/mnt/space/work/eapp-dart-domain")
      text("EAPP_KOTLIN_DOMAIN_DIR", value = "/mnt/space/work/eapp-kotlin-domain")
      text("EAPP_SWIFT_DOMAIN_DIR", value = "/mnt/space/work/eapp-swift-domain")
    }

    container(displayName = "Configure Source Directories", image = "amazoncorretto:17-alpine") {

        env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
        env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
      
        kotlinScript { api ->

            val EAPP_PROTO_SRC_DIR = System.getenv("EAPP_PROTO_SRC_DIR")
            
            val PROTO_INCLUDE_DIRS = """
                ${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/entities/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/galaxy/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/machine/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/organisation/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/space/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/universe/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/action/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page_para/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/context/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/knowledge/*.proto,
            """.trimIndent()
            
            var PROTO_INCLUDES = ""
            PROTO_INCLUDE_DIRS.split(",").forEach {
                var temp = it.trim()
                temp = temp.trim()
                PROTO_INCLUDES = "$PROTO_INCLUDES $temp"
            }

            api.parameters["PROTO_INCLUDES"] = PROTO_INCLUDES
        }

        requirements {
            workerTags("windows-pool")
        }
    }

    container(displayName = "Setup Version", image = "amazoncorretto:17-alpine") {
        kotlinScript { api ->
            // Get the current year and month
            val currentYear = (LocalDate.now().year % 100).toString().padStart(2, '0')
            val currentMonth = LocalDate.now().monthValue.toString()

            // Get the execution number from environment variables
            val currentExecution = System.getenv("JB_SPACE_EXECUTION_NUMBER")

            // Set the VERSION_NUMBER parameter
            api.parameters["VERSION_NUMBER"] = "$currentYear.$currentMonth.$currentExecution"
        }

        requirements {
            workerTags("windows-pool")
        }
    }

    parallel {

        sequential {
            
            container(displayName = "Python Domain Build", image = "python:3.9.16") {

                env["EAPP_PYTHON_DOMAIN_DIR"] = "{{ EAPP_PYTHON_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """
                  
                  	echo "--------------------------------------------------------------------"
                  	echo "Ensure you can run pip from the command line"
                  	echo "--------------------------------------------------------------------"
                  	python3 -m pip --version
                   	python3 -m ensurepip --default-pip
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Ensure pip, setuptools, and wheel are up to date"
                    echo "--------------------------------------------------------------------"
                    python3 -m pip install --upgrade pip setuptools wheel
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Install required packages for python build"
                    echo "--------------------------------------------------------------------"
                    python3 -m pip install protobuf
                    python3 -m pip install grpcio-tools
                    python3 -m pip install grpcio
                    python3 -m pip install twine
                    echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Remove previous PYTHON DOMAIN"
                   	echo "--------------------------------------------------------------------"
                   	# rm -rf ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos
                   	# rm -rf ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/gramx
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Python Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_PYTHON_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Python Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the python domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    python3 -m grpc_tools.protoc \
                      --python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      --grpc_python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      --pyi_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    find ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos -type d -exec touch {}/__init__.py \;
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Change directory to eapp-python-domain"
                    echo "--------------------------------------------------------------------"
                    cd /mnt/space/work/eapp-python-domain
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Configure pypirc"
                    echo "--------------------------------------------------------------------"
                    cp /mnt/space/work/eapp-python-domain/pypirc ~/.pypirc
                    sed "10s/.*/    version='{{ VERSION_NUMBER }}',/" ${'$'}EAPP_PYTHON_DOMAIN_DIR/setup.py > ${'$'}EAPP_PYTHON_DOMAIN_DIR/newsetup.py
                    mv ${'$'}EAPP_PYTHON_DOMAIN_DIR/newsetup.py ${'$'}EAPP_PYTHON_DOMAIN_DIR/setup.py
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build Package"
                    echo "--------------------------------------------------------------------"
                    python3 setup.py sdist
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Inspect Package"
                    echo "--------------------------------------------------------------------"
                    tar -tvf /mnt/space/work/eapp-python-domain/dist/ethos-{{ VERSION_NUMBER }}.tar.gz
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Publish Package"
                    echo "--------------------------------------------------------------------"
                    twine upload -r local dist/*
                    echo "--------------------------------------------------------------------"
                    
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }
        }	 // end of python domain sequential build

        sequential {
            container(displayName = "Nodejs Domain Build", image = "node") {

                env["EAPP_NODEJS_DOMAIN_DIR"] = "{{ EAPP_NODEJS_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_NODEJS_OUT_DIR"] = "{{ EAPP_PROTO_NODEJS_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """

                   	echo "--------------------------------------------------------------------"
                   	echo "Remove previous NODEJS DOMAIN"
                   	echo "--------------------------------------------------------------------"
                   	rm -rf ${'$'}EAPP_PROTO_NODEJS_OUT_DIR/ethos
                   	rm -rf ${'$'}EAPP_PROTO_NODEJS_OUT_DIR/gramx
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Nodejs Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_NODEJS_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Nodejs Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_NODEJS_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the javascript domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    grpc_tools_node_protoc \
                      --js_out=import_style=commonjs,binary:${'$'}EAPP_PROTO_NODEJS_OUT_DIR \
                      --grpc_out=grpc_js:${'$'}EAPP_PROTO_NODEJS_OUT_DIR \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }	// end of container job
        }	// end of nodejs domain sequential build

        sequential {
             container(displayName = "Dart Domain Build", image = "50gramx.registry.jetbrains.space/p/main/ethosindiacontainers/web-base:latest") {

                env["EAPP_DART_DOMAIN_DIR"] = "{{ EAPP_DART_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_DART_OUT_DIR"] = "{{ EAPP_PROTO_DART_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """

                  	echo "--------------------------------------------------------------------"
                  	echo "Ensure you can run dart from the command line"
                  	echo "--------------------------------------------------------------------"
                  	which dart
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Install the protocol compiler plugin for Dart"
                    echo "--------------------------------------------------------------------"
                    dart pub global activate protoc_plugin
                    export PATH="${'$'}PATH":"${'$'}HOME/.pub-cache/bin"
                    env
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Ensure protocol compiler version is 3+"
                    echo "--------------------------------------------------------------------"
                    apt install -y protobuf-compiler
                    protoc --version
                    echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Remove previous DART DOMAIN"
                   	echo "--------------------------------------------------------------------"
                   	rm -rf ${'$'}EAPP_PROTO_DART_OUT_DIR/ethos
                   	rm -rf ${'$'}EAPP_PROTO_DART_OUT_DIR/gramx
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing dart Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_DART_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing dart Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_DART_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the dart domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    protoc \
                      --dart_out=grpc:${'$'}EAPP_PROTO_DART_OUT_DIR/lib \
                      google/protobuf/timestamp.proto \
                      google/protobuf/any.proto \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Change directory to eapp-dart-domain"
                    echo "--------------------------------------------------------------------"
                    cd ${'$'}EAPP_PROTO_DART_OUT_DIR
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Configure pubspec.yaml"
                    echo "--------------------------------------------------------------------"
                    sed "3s/.*/version: {{ VERSION_NUMBER }}/" ${'$'}EAPP_PROTO_DART_OUT_DIR/pubspec.yaml > ${'$'}EAPP_PROTO_DART_OUT_DIR/newpubspec.yaml
                    mv ${'$'}EAPP_PROTO_DART_OUT_DIR/newpubspec.yaml ${'$'}EAPP_PROTO_DART_OUT_DIR/pubspec.yaml
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Publish Dart Package"
                    echo "--------------------------------------------------------------------"
                    dart pub token list
                    dart pub token add https://dart.pkg.jetbrains.space/50gramx/p/main/dart-delivery --env-var JB_SPACE_CLIENT_TOKEN
                    dart pub token list
                    dart pub publish --skip-validation
                    echo "--------------------------------------------------------------------"
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }
        }	// end of dart domain sequential build


        sequential {
             container(displayName = "Kotlin Domain Build", image = "node") {

                env["EAPP_KOTLIN_DOMAIN_DIR"] = "{{ EAPP_KOTLIN_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_KOTLIN_OUT_DIR"] = "{{ EAPP_PROTO_KOTLIN_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """
                   	echo "--------------------------------------------------------------------"
                   	echo "Remove previous KOTLIN DOMAIN"
                   	echo "--------------------------------------------------------------------"
                   	rm -rf ${'$'}EAPP_PROTO_KOTLIN_OUT_DIR/ethos
                   	rm -rf ${'$'}EAPP_PROTO_KOTLIN_OUT_DIR/gramx
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Kotlin Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_KOTLIN_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Kotlin Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_KOTLIN_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the kotlin domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    # GENERATING PROTO FILES
                    protoc --plugin=protoc-gen-grpckt=/opt/ethos/data/kotlin/protoc-gen-grpc-kotlin.sh \
                      --grpckt_out=${'$'}EAPP_PROTO_KOTLIN_OUT_DIR  \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }
        }	// end of kotlin domain sequential build

        sequential {
             container(displayName = "Swift Domain Build", image = "node") {

                env["EAPP_SWIFT_DOMAIN_DIR"] = "{{ EAPP_SWIFT_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_SWIFT_OUT_DIR"] = "{{ EAPP_PROTO_SWIFT_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """
                   	echo "--------------------------------------------------------------------"
                   	echo "Remove previous SWIFT DOMAIN"
                   	echo "--------------------------------------------------------------------"
                   	rm -rf ${'$'}EAPP_PROTO_SWIFT_OUT_DIR/ethos
                   	rm -rf ${'$'}EAPP_PROTO_SWIFT_OUT_DIR/gramx
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Swift Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_SWIFT_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Swift Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_SWIFT_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the Swift domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    protoc --include_imports \
                      --include_source_info \
                      --descriptor_set_out=${'$'}EAPP_PROTO_SWIFT_OUT_DIR/api_descriptor.pb \
                      --swift_out=${'$'}EAPP_PROTO_SWIFT_OUT_DIR \
                      --grpc-swift_out=Client=true,Server=false:${'$'}EAPP_PROTO_SWIFT_OUT_DIR \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }
        }	// end of swift domain sequential build
        
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

                    echo "copy steps"
                    cp -rf \
                        ${'$'}EAPP_PYTHON_DOMAIN_DIR/src/tests/ethos/elint/services/product/identity/account/access_account/steps \
                        ${'$'}EAPP_CORE_DOMAIN_DIR/src/main/features/ethos/elint/services/product/identity/account/access_account/features
                    # end of copying steps

                    echo "start tests"
                    behave ${'$'}EAPP_CORE_DOMAIN_DIR/src/main/features/ethos/elint/services/product/identity/account/access_account/features
                    # end of running tests

                  """
                }   // end of execution

                requirements {
                    workerTags("windows-pool")
                }

            }   // end of running Python Domain Capability Contract Behaviour Tests
        }	// end of python domain implemented capability contract behaviour acceptance
    }	// end of all domain parallel build
}


job("Distribute Core Dev Domain Packages") {
  
	startOn {
        gitPush {
            anyBranchMatching {
                +"entities/*"
            }
        }
    }

    // check out eapp-python-domain to /mnt/space/work/eapp-python-domain
    git("eapp-python-domain")

	parameters {
      text("EAPP_PROTO_SRC_DIR", value = "/mnt/space/work/eapp-service-core/src/main/proto")
      text("EAPP_PROTO_PYTHON_OUT_DIR", value = "/mnt/space/work/eapp-python-domain/src/eapp_python_domain")

      text("EAPP_CORE_DOMAIN_DIR", value = "/mnt/space/work/eapp-service-core")
      text("EAPP_PYTHON_DOMAIN_DIR", value = "/mnt/space/work/eapp-python-domain")
    }

    container(displayName = "Configure Source Directories", image = "amazoncorretto:17-alpine") {

        env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
        env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
      
        kotlinScript { api ->

            val EAPP_PROTO_SRC_DIR = System.getenv("EAPP_PROTO_SRC_DIR")
            
            val PROTO_INCLUDE_DIRS = """
                ${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/entities/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/galaxy/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/machine/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/organisation/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/space/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/universe/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/action/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account_assistant/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page_para/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/context/*.proto,
                ${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/knowledge/*.proto,
            """.trimIndent()
            
            var PROTO_INCLUDES = ""
            PROTO_INCLUDE_DIRS.split(",").forEach {
                var temp = it.trim()
                temp = temp.trim()
                PROTO_INCLUDES = "$PROTO_INCLUDES $temp"
            }

            api.parameters["PROTO_INCLUDES"] = PROTO_INCLUDES
        }

        requirements {
            workerTags("windows-pool")
        }
    }

    container(displayName = "Setup Version", image = "amazoncorretto:17-alpine") {
        kotlinScript { api ->
            // Get the current year and month
            val currentYear = (LocalDate.now().year % 100).toString().padStart(2, '0')
            val currentMonth = LocalDate.now().monthValue.toString()

            // Get the execution number from environment variables
            val currentExecution = System.getenv("JB_SPACE_EXECUTION_NUMBER")

            // Set the VERSION_NUMBER parameter
            api.parameters["VERSION_NUMBER"] = "$currentYear.$currentMonth.$currentExecution"
        }

        requirements {
            workerTags("windows-pool")
        }
    }

    parallel {

        sequential {
            
            container(displayName = "Python Dev Domain Build", image = "python:3.9.16") {

                env["EAPP_PYTHON_DOMAIN_DIR"] = "{{ EAPP_PYTHON_DOMAIN_DIR }}"
                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
              	env["PROTO_INCLUDES"] = "{{ PROTO_INCLUDES }}"
            	env["SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY"] = Secrets("SERVICE_CORE_CONTRACT_APP_SSH_PRIVATE_KEY")

                shellScript {
                  content = """
                  
                  	echo "--------------------------------------------------------------------"
                  	echo "Ensure you can run pip from the command line"
                  	echo "--------------------------------------------------------------------"
                  	python3 -m pip --version
                   	python3 -m ensurepip --default-pip
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Ensure pip, setuptools, and wheel are up to date"
                    echo "--------------------------------------------------------------------"
                    python3 -m pip install --upgrade pip setuptools wheel
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Install required packages for python build"
                    echo "--------------------------------------------------------------------"
                    python3 -m pip install protobuf
                    python3 -m pip install grpcio-tools
                    python3 -m pip install grpcio
                    python3 -m pip install twine
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Listing Mount Directory"
                    echo "--------------------------------------------------------------------"
                   	ls /mnt
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Mounted Space Work Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls /mnt/space/work
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Python Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_PYTHON_OUT_DIR
                   	echo "--------------------------------------------------------------------"

                   	echo "--------------------------------------------------------------------"
                   	echo "Listing Python Out Directory"
                   	echo "--------------------------------------------------------------------"
                   	ls ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos
                   	echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build the python domain proto client codes"
                    echo "--------------------------------------------------------------------"
                    echo ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    python3 -m grpc_tools.protoc \
                      --python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      --grpc_python_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      --pyi_out=${'$'}EAPP_PROTO_PYTHON_OUT_DIR \
                      -I ${'$'}EAPP_PROTO_SRC_DIR \
                      --proto_path ${'$'}PROTO_INCLUDES
                    echo "--------------------------------------------------------------------"
                    find ${'$'}EAPP_PROTO_PYTHON_OUT_DIR/ethos -type d -exec touch {}/__init__.py \;
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Change directory to eapp-python-domain"
                    echo "--------------------------------------------------------------------"
                    cd /mnt/space/work/eapp-python-domain
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Configure pypirc"
                    echo "--------------------------------------------------------------------"
                    cp /mnt/space/work/eapp-python-domain/pypirc ~/.pypirc
                    sed "10s/.*/    version='{{ VERSION_NUMBER }}',/" ${'$'}EAPP_PYTHON_DOMAIN_DIR/setup_dev.py > ${'$'}EAPP_PYTHON_DOMAIN_DIR/newsetup.py
                    mv ${'$'}EAPP_PYTHON_DOMAIN_DIR/newsetup.py ${'$'}EAPP_PYTHON_DOMAIN_DIR/setup.py
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Build Package"
                    echo "--------------------------------------------------------------------"
                    python3 setup.py sdist
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Inspect Package"
                    echo "--------------------------------------------------------------------"
                    tar -tvf /mnt/space/work/eapp-python-domain/dist/ethosdev-{{ VERSION_NUMBER }}.tar.gz
                    echo "--------------------------------------------------------------------"

                    echo "--------------------------------------------------------------------"
                    echo "Publish Package"
                    echo "--------------------------------------------------------------------"
                    twine upload -r local dist/*
                    echo "--------------------------------------------------------------------"
                    
                  """
                }

                requirements {
                    workerTags("windows-pool")
                }
            }
        }	 // end of python dev domain sequential build
        
    }	// end of all domain parallel build
}
//end

