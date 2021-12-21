If you need Kafka UI - deploy kafdrop using oc apply -f kafdrop.yaml

You can find more information about Apache Camel and Apache Camel K on the [official Camel website](https://camel.apache.org).


## Before you begin

Read the general instructions in the [root README.md file](../README.md) for setting up your environment and the Kubernetes cluster before looking at this example.

Make sure you've read the [installation instructions](https://camel.apache.org/camel-k/latest/installation/installation.html) for your specific 
cluster before starting the example.

You should open this file with [Didact](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-didact) if available on your IDE.

## Requirements


**Kubectl CLI**

The Kubernetes `kubectl` CLI tool will be used to interact with the Kubernetes cluster.

[Check if the Kubectl CLI is installed]

*Status: unknown*{#kubectl-requirements-status}

**Connection to a Kubernetes cluster**

You need to connect to a Kubernetes cluster in order to run the example.

[Check if you're connected to a Kubernetes cluster]

*Status: unknown*{#cluster-requirements-status}

**Apache Camel K CLI ("kamel")**

You need the Apache Camel K CLI ("kamel") in order to access all Camel K features.

[Check if the Apache Camel K CLI ("kamel") is installed]
*Status: unknown*{#kamel-requirements-status}

### Optional Requirements

The following requirements are optional. They don't prevent the execution of the demo, but may make it easier to follow.

**VS Code Extension Pack for Apache Camel**

The VS Code Extension Pack for Apache Camel provides a collection of useful tools for Apache Camel K developers,
such as code completion and integrated lifecycle management. They are **recommended** for the tutorial, but they are **not**
required.

You can install it from the VS Code Extensions marketplace.

[Check if the VS Code Extension Pack for Apache Camel by Red Hat is installed]

*Status: unknown*{#extension-requirement-status}

## 1. Preparing the namespace

Let's open a terminal and go to the  directory:

`
We're going to create a namespace named `camel-basic` for running the example. To create it, execute the following command:

```
kubectl create namespace camel-basic
```


Now we can set the `camel-basic` namespace as default namespace for the following commands:

```
kubectl config set-context --current --namespace=camel-basic
```


You need to install Camel K in the `camel-basic` namespace (or globally in the whole cluster).
In many settings (e.g. OpenShift, CRC), it's sufficient to execute the following command to install Camel K:

```
kamel install
```


NOTE: The `kamel install` command requires some prerequisites to be successful in some situations, e.g. you need to enable the registry addon on Minikube. Refer to the [Camel K install guide](https://camel.apache.org/camel-k/latest/installation/installation.html) for cluster-specific instructions.

To check that Camel K is installed we'll retrieve the IntegrationPlatform object from the namespace:

```
kubectl get integrationplatform
```


You should find an IntegrationPlatform in status `Ready`.

You can now proceed to the next section.

## 2. Running a basic integration


Use the following command to run it in "dev mode", in order to see the logs in the integration terminal:

```
kamel run EventNotifier.java --dev
kamel run EventTransformer.java --dev
```
([^ execute](didact://?commandId=vscode.didact.sendNamedTerminalAString&text=camelTerm$$kamel%20run%20Basic.java%20--dev&completion=Camel%20K%20basic%20integration%20run%20in%20dev%20mode. "Opens a new terminal and sends the command above"){.didact})

If everything is ok, after the build phase finishes, you should see the Camel integration running and continuously printing "Hello World!..." in the terminal window.

When running in dev mode, you can change the integration code and let Camel K redeploy the changes automatically.


> **Note:** When you terminate a "dev mode" execution, also the remote integration will be deleted. This gives the experience of a local program execution, but the integration is actually running in the remote cluster.

To keep the integration running and not linked to the terminal, you can run it without "dev mode", just run:

```
kamel run EventNotifier.java 
kamel run EventTransformer.java 
```


After executing the command, you should be able to see it among running integrations:

```
kubectl get integrations
```


Even if it's not running in dev mode, you can still see the logs of the integration using the following command:

```
kamel log <integration_name>
```
