    pipeline { 
        agent {
            docker { 
                //image 'maven:3.6-alpine'
                //image 'oracle/graalvm-ce'
                image 'quay.io/quarkus/centos-quarkus-maven:19.0.2'
                //image 'quay.io/quarkus/centos-quarkus-native-image'
                args '-u root:root'
                //args '-v $HOME/.m2:/root/.m2'
            }
        }
        stages {
            stage('build native + test'){
                steps {
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'b258ad50-12de-48cd-9ab5-ff94699c6792', url: 'https://github.com/rschumm/halloquarkus.git']]])
                    
                    sh 'mvn clean verify -Pnative' //-DskipTests' 
                    //sh 'mvn package -Pnative -Dnative-image.docker-build=true' 
                }
            }
            stage('deploy'){
                steps {
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'b258ad50-12de-48cd-9ab5-ff94699c6792', url: 'https://github.com/rschumm/operators.git]]])
                    sh 'yum install ansible -y'   
                    //echo "185.203.42.155 ansible_user=webapp-user ansible_password=java123app ansible_ssh_common_args='-o StrictHostKeyChecking=no' " > hostfile  
                    sh 'ansible-playbook -i ansible/hostfile ansible/deploy_webapp/deploywebapp.yml'  
                }
            }
        }
    }

