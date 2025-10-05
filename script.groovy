def buildApp() {
    echo "Building and Testing the application..."
    // Add build and test commands here
}

def testApp() {} {
    echo "Running tests..."
    // Add test commands here
}

def deployApp() {
    echo "Deploying the application..."
   echo "Deploying version: ${params.VERSION}"
    // Add deploy commands here
}
return this