# khs-trouble-maker-client 

Servlet that is registered with clients of the [Trouble Maker](https://github.com/in-the-keyhole/khs-trouble-maker) framework used to randomly produce server trouble issues.

Installation
------------
Add this dependency to your POM.XML:

	<dependency>
		<groupId>com.keyholesoftware</groupId>
		<artifactId>khs-trouble-maker-client</artifactId>
		<version>1.0.1-SNAPSHOT</version>
	</dependency>	


Register this Java Servlet in your web.xml or config:


	<servlet>
		 <servlet-name>trouble</servlet-name>
		  <servlet-class>khs.trouble.servlet</servlet-class>
		     <init-param>
	            <param-name>token</param-name>
	            <!-- token should match dashboard token -->
	            <param-value>abc123</param-value>
	        </init-param>
		</servlet>
		<servlet-mapping>
		    <servlet-name>trouble</servlet-name>
		    <url-pattern>/trouble/*</url-pattern>
	</servlet-mapping>
	
Actions
-------	
By default, Trouble Maker actions will be performed by calling the following URIs, if a matching access token is defined in the request header. The Trouble Maker [dashboard](https://github.com/in-the-keyhole/khs-trouble-maker) will invoke these URIs. 

`http://<server>/trouble/kill` - Kills the service with a System.exit() command. 

`http://<server>/trouble/memory` - Executes a thread that fills up heap memory and keeps it there for the timeout period.

`http://<server>/trouble/load` - Spawns specified number of threads the block for the the timeout period.

`http://<server>/trouble/exception` - Throws an exception to validate exception handling behavior of a service.


Defining Custom Actions Code Blocks
-----------------------------------
If you want to apply your own trouble actions and override the supplied defaults, yYou can create a class that extends from this supplied abstract class, as shown below. 

	public class MyKillCodeBlock extends BaseCodeBlokc{	
		public KillBlock(long timeout) {
		super(timeout);
	 }
		@Override
		public void eval() {
			// Do stuff to kill this process...like..
			System.exit(-1)
		}	
	}

Then you will need to register the action as a parameter for the trouble servlet when defining it in the web.xml. The example below shows how a custom load action is registered:
 
    ...
	<servlet>
		 <servlet-name>trouble</servlet-name>
		  <servlet-class>khs.trouble.servlet</servlet-class>
			...
	        <init-param>
	           <!-- Overriding the Kill Action Block -->
	            <param-name>kill</param-name>
	            <param-value>com.mytrouble.MyKill</param-value>
	        </init-param>
	 
	</servlet>
	...

Here's the kill operation names you can use to override, through servlet init parameters:
* kill
* load
* memory
* exception 
