# Syndesis QE
Updated: 11.9.2018

##### Table of Contents
- [Structure](#structure)
- [Prepare minishift instance](#prepare-minishift-instance)
- [Scenarios](#scenarios)
- [Configuration](#configuration)
- [Execution](#execution)
- [Running on remote OpenShift instance](#running-on-remote-openshift-instance)
- [Creating a Pull Request](#creating-a-pull-request)

### Structure

```bash
├── docs
├── rest-tests
├── ui-tests
├── ui-tests-protractor
└── utilities
```

#### docs
On-going initiative to provide comprehensive guide to current code structure.

#### rest-tests
Java based tests that use Cucumber scenarios.
Test actions are executed directly to `syndesis-rest` backend.

#### ui-tests
Java based tests that use Selenide and Cucumber BDD scenarios.
Test actions are mainly UI driven with additional 3rd party validation like Salesforce, Twitter etc.

#### ui-tests-protractor (Deprecated)
Typescript based tests that use Protractor and Cucumber BDD scenarios.

### Prepare minishift instance

#### Prerequisites:
Installed [Minishift](https://www.openshift.org/minishift/)

Cloned [Syndesis](https://github.com/syndesisio/syndesis)

Cloned this repo with [syndesis-extension](https://github.com/syndesisio/syndesis) submodule (*git clone --recurse-submodules*)

Correctly set test.properties and credentials.json (described later)

Added FUSE repositories to maven due to dependencies

Before you import maven project to the IDE, you have to installed *Lombok* and *Cucumber* plugins to the IDE.

For more information ask mcada@redhat.com or avano@redhat.com or tplevko@redhat.com

#### Create a minishift instance
For minishift version 23+
    
    minishift start 
    
For version 22 and below 

    minishift start --memory 4912 --cpus 2 --disk-size 20GB --openshift-version v3.9.0

#### Add minishift ip to the hosts
Following lines in /etc/hosts file, insert your minishift ip:

	${minishift_ip} syndesis.my-minishift.syndesis.io
	${minishift_ip} todo-syndesis.my-minishift.syndesis.io

Due to --route option when installing syndesis and updated /etc/hosts file we don't
have to update all third party applications and their callbacks for every minishift/openshift IP.

#### Add admin rights to developer user
    oc adm policy --as system:admin add-cluster-role-to-user cluster-admin developer

### Scenarios
Test scenarios are provided in Gherkin language in a BDD fashion. Located in `./resources`
directory of `*-tests` module, e.g. [UI scenarios](https://github.com/syndesisio/syndesis-qe/tree/master/ui-tests/src/test/resources/features).

Every scenario is wrapped with appropriate tags to target specific execution on demand.

### Configuration
NOTE: Successful execution of tests requires fully configured credentials.
All the callback URLs, Oauth tokens, etc. for Salesforce and Twitter accounts.

#### Example of test.properties to run on minishift
File `test.properties` should be located in root of syndesis-qe folder.
Working example can be found in jenkins nightly build run logs.

For more information ask mcada@redhat.com or avano@redhat.com or tplevko@redhat.com

test.properties (update *syndesis.config.openshift.url* property according to your minishift ip)
```
syndesis.config.openshift.url=https://192.168.64.2:8443
syndesis.config.openshift.namespace=syndesis
syndesis.config.openshift.route.suffix=my-minishift.syndesis.io
# namespace for the subject access review (if not specified, will use the same namespace as for the deployment)
syndesis.config.openshift.sar_namespace=syndesis

syndesis.dballocator.url=http://dballocator.mw.lab.eng.bos.redhat.com:8080

#timeout in seconds, if not set default is 300
syndesis.config.timeout=300

#delay in seconds, if not set default is 1
jenkins.delay=7

syndesis.config.ui.url=https://syndesis.my-minishift.syndesis.io
syndesis.config.ui.username=developer
syndesis.config.ui.password=developer
syndesis.config.ui.browser=firefox
```

#### Example of credentials.json
File `credentials.json` should be located in root of syndesis-qe folder.
Working example on demand.

For more information ask mcada@redhat.com or avano@redhat.com or tplevko@redhat.com

credentials.json
```json
{
  "twitter_listener": {
    "service": "twitter",
    "properties": {
      "screenName": "****",
      "accessToken": "****",
      "accessTokenSecret": "****",
      "consumerKey": "****",
      "consumerSecret": "*****",
      "login": "****",
      "password": "****"
    }
  },
  "twitter_talky": {
    "service": "twitter",
    "properties": {
      "screenName": "****",
      "consumerKey": "****",
      "consumerSecret": "****",
      "accessToken": "****",
      "accessTokenSecret": "****",
      "login": "****",
      "password": "****"
    }
  },
  "salesforce": {
    "service": "salesforce",
    "properties": {
      "instanceUrl": "https://developer.salesforce.com",
      "loginUrl": "https://login.salesforce.com",
      "clientId": "****",
      "clientSecret": "****",
      "userName": "****",
      "password": "****"
    }
  },
  "QE Salesforce": {
    "service": "salesforce",
    "properties": {
      "instanceUrl": "https://developer.salesforce.com",
      "loginUrl": "https://login.salesforce.com",
      "clientId": "****",
      "clientSecret": "****",
      "userName": "****",
      "password": "****"
    }
  },
  "Twitter Listener": {
    "service": "twitter",
    "properties": {
      "screenName": "****",
      "accessToken": "****",
      "accessTokenSecret": "****",
      "consumerKey": "****",
      "consumerSecret": "****"
    }
  },
  "s3": {
    "service": "s3",
    "properties": {
      "region": "****",
      "accessKey": "****",
      "secretKey": "****"
    }
  },
  "syndesis": {
    "service": "syndesis",
    "properties": {
      "instanceUrl": "****",
      "login": "****",
      "password": "****"
    }
  },
  "QE Dropbox": {
    "service": "dropbox",
    "properties": {
      "accessToken": "****",
      "clientIdentifier": "****"
    }
  },
  "ftp": {
    "service": "ftp",
    "properties": {
      "binary": "Yes",
      "connectTimeout": "10000",
      "disconnect": "No",
      "host": "ftpd",
      "maximumReconnectAttempts": "3",
      "passiveMode": "Yes",
      "password": "****",
      "port": "****",
      "reconnectDelay": "1000",
      "timeout": "30000",
      "username": "****"
    }
  },
  "QE Slack": {
    "service": "slack",
    "properties": {
      "webhookUrl": "****",
      "Token": "****"
    }
  },
  "QE Google Mail": {
    "service": "Google Mail",
    "properties": {
      "clientId": "****",
      "clientSecret": "****",
      "applicationName": "****",
      "refreshToken": "****",
      "userId": "****",
      "email": "****",
      "password": "****"
    }
  },
  "telegram": {
    "service": "telegram",
    "properties": {
      "authorizationToken": "****"
    }
  },
  "GitHub": {
    "service": "GitHub",
    "properties": {
      "PersonalAccessToken": "****"
    }
  }
}
```
for ftp connection credentials:
All values are just examples / proposals. Need to be updated in accordance with 

### Execution

#### Before execution
For the test execution at least `syndesis-rest` modules are required in current SNAPSHOT version.

```
cd <syndesis-project-dir>
./syndesis/tools/bin/syndesis build --init --batch-mode --backend --flash
```

Working with extensions requires syndesis-extensions submodule compiled. 
**NOTE** If you didn't clone syndesis-qe repository with *--recurse-submodules* option as mentioned before, you have to clone
 syndesis-extensions from this [repo](https://github.com/syndesisio/syndesis-extensions).
```
cd <syndesis-qe-project-dir>/syndesis-extensions
mvn clean install
```

#### Test suite execution

There're three Maven profiles: `all, rest, ui` to target the specific test suite.

```
mvn clean test // default all profile
mvn clean test -P ui
mvn clean test -P rest
```

#### Particular test execution

When you want to run only the particular tests or scenarios, just use their **tags**. The following example runs @integration-ftp-ftp scenario from the rest suite

```
mvn clean test -P rest -Dcucumber.options="--tags @integration-ftp-ftp"
```

#### Additional parameters

```
mvn clean test -P rest -Dcucumber.options="--tags @integration-ftp-ftp" \
        -Dsyndesis.config.openshift.namespace.lock=true \
        -Dsyndesis.config.openshift.namespace.cleanup=true \
        -Dsyndesis.config.openshift.namespace.cleanup.after=false
```
You can use various parameters. From the previous command, parameter:
* *syndesis.config.openshift.namespace.lock* - lock the namespace
* *syndesis.config.openshift.namespace.cleanup* - cleanup namespace before the tests
* *syndesis.config.openshift.namespace.lock* - cleanup namespace after the tests (it can be useful to set this to false during debugging phase)

**NOTE** - if you set syndesis.config.openshift.namespace.lock parameter to true and you stop tests during running, the lock will not be released! 
It causes that the next tests stuck for the 60 minutes on **Waiting to obtain namespace lock**. If you don't want to
wait, just delete *test-lock* secret and *syndesis* project from minishift and run the tests again.

```
oc login -u developer -p developer
oc delete secret test-lock
oc delete project syndesis
```

To select syndesis version, add another maven parameter:

	-Dsyndesis.config.template.version=<version>

To install syndesis from operator template, add maven parameter:

	-Dsyndesis.config.operator.url=<url-to-operator.yml>

When testsuite output text says `Waiting for Syndesis to get ready`, you can kill mvn execution
as the deployment process already started and the run is no longer required.

#### Debugging

When you want to debug code, just add following command
```
"-Dmaven.surefire.debug=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE"
```
to the maven run command.

E.g. for debugging slack tests:
```
mvn "-Dmaven.surefire.debug=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE" \
        clean test -P ui "-Dcucumber.options=--tags @slack" \
        -Dsyndesis.config.openshift.namespace.lock=false \
        -Dsyndesis.config.openshift.namespace.cleanup=true \
        -Dsyndesis.config.openshift.namespace.cleanup.after=false
```

After that, the project will be waiting for a connection. After that, you can connect to remote debug in IDE. For more information [look here](http://jtuts.com/2016/07/29/how-to-set-up-remote-debugging-in-intellij-idea-for-a-webapp-that-is-run-by-tomcat-maven-plugin/).

### Running on remote OpenShift instance
In some cases, you want to run the tests on the remote OpenShift instance instead of the local Minishift instance.

Lets say, you have a OpenShift instance on *https://myFancyOpenshiftInstance.mydomain.com* and you have user 
**remoteuser** with password **RemoteUserPassword**.

If you want to run the tests on the existing remote OpenShift instance, follow this steps.

* Connect to OpenShift instance. E.g.
  ```
  oc login https://myFancyOpenshiftInstance.mydomain.com:8443 -u remoteuser -p RemoteUserPassword
  ```
* First, create a new namespace for testing. E.g. **testingnamespace**
  ```
  oc create project testingnamespace
  ```
* After that, update *test.properties* according to the remote instance. E.g.
    ```
    syndesis.config.openshift.url=https://myFancyOpenshiftInstance.mydomain.com:8443
    syndesis.config.openshift.namespace=testingnamespace
    syndesis.config.openshift.route.suffix=my-minishift.syndesis.io
    # namespace for the subject access review (if not specified, will use the same namespace as for the deployment)
    syndesis.config.openshift.sar_namespace=testingnamespace

    syndesis.dballocator.url=http://dballocator.mw.lab.eng.bos.redhat.com:8080
    
    #timeout in seconds, if not set default is 300
    syndesis.config.timeout=300
    
    #delay in seconds, if not set default is 1
    jenkins.delay=7
    
    syndesis.config.ui.url=https://testingnamespace.my-minishift.syndesis.io
    syndesis.config.ui.username=remoteuser
    syndesis.config.ui.password=RemoteUserPassword
    syndesis.config.ui.browser=firefox
    ```
* You have to also update */etc/hosts* according to the remote instance. E.g.
    ```
	${remote_openshift_ip} testingnamespace.my-minishift.syndesis.io
	${remote_openshift_ip} todo-testingnamespace.my-minishift.syndesis.io
	```
After that, you can run the tests on the remote OpenShift instances. 
Remember to delete project after testing.
### Creating a Pull Request

When you create a PR on GitHub a new Jenkins job is scheduled. This job runs on Fuse QE Jenkins instance and runs a basic subset of tests (annotated with @smoke tag).

If you want to run a different subset of tests, you can use `//test: @mytag1,@mytag2` in your PR description to trigger specific tests annotated by given tags.

If you don't want to run the job for your PR at all (for example when you are changing small things in README file), you can use `//skip-ci` in your PR description.

When the PR job fails because of test failures and you believe that you didn't cause the error, you can try to trigger the job once again. For this just comment `retest this please` in the PR and a new build will be triggered in few minutes.

Please remember that each time you push something new (or amend something old) in the PR, a new build is triggered automatically, so you don't need to do anything else to get your PR rebuilt.
