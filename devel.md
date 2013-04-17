Development guide
=================

Full dependency list for development and deployment
---------------------------------------------------

 * Java JDK 1.6 or later, http://openjdk.java.net/
 * Git SCM, http://git-scm.org
 * Apache Ant, http://ant.apache.org
 * Apache Maven, http://maven.apache.org/
 * 

Setting up windows development enviroment
-----------------------------------------

 * Install dependencies:

     * Java JDK, http://www.oracle.com/technetwork/java/javase/downloads/
     * Apache ant, http://ant.apache.org
     * Apache Maven, http://maven.apache.org

 * Add all binaries to PATH, system should be able to invoke 'ant', 'mvn', 'java' and 'javac'
 * Set JAVA_HOME variable to Your JDK installation path.

 * Clone cleer repository
 * Update git submodules with 'git submodule init' and, after it -- 'git submodule update'.
 * Move to submodules/jline2 directory and invoke 'mvn install -DskipTests' from there.
 * Move to cleer root folder (cd ../..)
 * Invoke 'and desktop'. 
 * You can find compiled jar in tmp/product/desktop folder

Setting up linux (ubuntu) development enviroment
------------------------------------------------

    sudo apt-get install mvn ant openjdk-7-jdk p7zip-full  # Maybe, openjdk-6-jdk in old versions
    git clone <repository>
    cd cleer
    ant enviroment
    ant desktop
    
 After it, You're done. Open ide/eclipse-desktop in Eclipse or continue hacking code in command line, building new jar with `ant desktop` command.
