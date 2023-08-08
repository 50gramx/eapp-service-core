/*
flowchart TD
    A[Start] -->|Python Domain Build| B(Checkout Service Core)
    B -->|Done| C(Checkout Python Domain)
    C -->|Done| D(Configure Domain Directories)
    D -->|Done| E(Build Python Domain)
    E -->|Done| F(Push Python Domain)
    F -->|Done| G[End]
    A -->|Nodejs Domain Build| N(Checkout Service Core)
    N -->|Done| H(Checkout Nodejs Domain)
    H -->|Done| I(Configure Domain Directories)
    I -->|Done| J(Build Nodejs Domain)
    J -->|Done| K(Push Nodejs Domain)
    K -->|Done| L[End]
*/

job("Distribute Core Domain Packages") {
  
	startOn {
        gitPush {
            anyBranchMatching {
                +"release-*"
                +"master"
            }
        }
    }
    
	parallel {

        sequential {
            container(displayName = "Python Domain Build", image = "python:3") {
                shellScript {
                  content = """
                    pwd
                    ls -l
                  """
                }
            }
        }

        sequential {
            container(displayName = "Nodejs Domain Build", image = "node") {
                shellScript {
                  content = """
                    pwd
                    ls -l
                  """
                }
            }
        }
    }
}