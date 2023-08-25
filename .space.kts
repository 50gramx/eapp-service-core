/*
flowchart TD
    A[Start] -->|Python Domain Build| B(Checkout Service Core)
    B -->|Done| C(Checkout Python Domain)
    C -->|Done| D(Configure Domain Directories)
    D -->|Done| E(Build Python Domain)
    E -->|Done| F(Push Python Domain)
    F -->|Done| G(Ensure Hosted Python Index)
    G -->|Done| H(Build & Publish Python Package)
    H -->|Done| I[End]
    A -->|Nodejs Domain Build| N(Checkout Service Core)
    N -->|Done| O(Checkout Nodejs Domain)
    O -->|Done| P(Configure Domain Directories)
    P -->|Done| Q(Build Nodejs Domain)
    Q -->|Done| R(Push Nodejs Domain)
    R -->|Done| S(Ensure Hosted Nodejs Index)
    S -->|Done| T(Build & Publish NPM Package)
    T -->|Done| I[End]

*/

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
    }

    container(displayName = "Configure Directories", image = "amazoncorretto:17-alpine") {

        env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
        env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"
      
        kotlinScript { api ->

            val EAPP_PROTO_SRC_DIR = System.getenv("EAPP_PROTO_SRC_DIR")
            val EAPP_PROTO_PYTHON_OUT_DIR = System.getenv("EAPP_PROTO_PYTHON_OUT_DIR")
            
            val PROTO_INCLUDE_DIRS = """
                ${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
                ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1005/*.proto,
                ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1005/capability/*.proto,
                ${EAPP_PROTO_SRC_DIR}/gramx/seventy/zero/ethos/gramxpro/multiverse/core/entity/epe_1001/*.proto,
                ${EAPP_PROTO_SRC_DIR}/gramx/seventy/zero/ethos/gramxpro/multiverse/core/entity/epe_1002/*.proto
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
            
            container(displayName = "Configure Directories", image = "python:3") {

                env["EAPP_PROTO_SRC_DIR"] = "{{ EAPP_PROTO_SRC_DIR }}"
                env["EAPP_PROTO_PYTHON_OUT_DIR"] = "{{ EAPP_PROTO_PYTHON_OUT_DIR }}"

                shellScript {
                  content = """
                    export PROTO_INCLUDES=""
                    
                    export PROTO_INCLUDE_DIRS="${'$'}{EAPP_PROTO_SRC_DIR}/google/api/*.proto,"
                    
                    for it in $(echo ${'$'}{PROTO_INCLUDE_DIRS} | tr "," "\n"); do
                        TEMP="${'$'}{it}"
                        TEMP="$(echo -e "${'$'}{TEMP}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
                        PROTO_INCLUDES="${'$'}{PROTO_INCLUDES} ${'$'}{TEMP}"
                    done
                    
                    env

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
}