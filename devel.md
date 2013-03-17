Development guide
=================

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
