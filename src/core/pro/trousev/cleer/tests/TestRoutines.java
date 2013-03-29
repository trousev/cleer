package pro.trousev.cleer.tests;

/**
 * Это -- "маленькая и самописная JUnit", чтобы не тащить монструозную либу на устройства
 * 
 * @author doctor
 *
 */
public class TestRoutines {
	interface Test
	{
		public String name();
		public void run() throws Failure, Throwable;
	}
	class Failure extends Exception
	{
		private static final long serialVersionUID = 5464855874267237579L;
		Failure(String message)
		{
			super(message);
		}
	}
	void expect(boolean value, String failureReason) throws Failure
	{
		if(value == false)
			throw new Failure(failureReason);
	}
	void expectFalse(boolean value, String failureReason) throws Failure
	{
		if(value == true)
			throw new Failure(failureReason);
	}
	
	boolean expectException(Test test)
	{
		try
		{
			test.run();
			System.out.println("FAILED: "+test.name()+". Expected exception throwed, but nothing happened");
			return false;
		}
		catch (Throwable t)
		{
			System.out.println("PASSED: "+test.name());
			return true;
		}
	}
	
	boolean expectExecution(Test test)
	{
		try
		{
			test.run();
			System.out.println("PASSED: "+test.name());
			return false;
		}
		catch (Failure f)
		{
			System.out.println("FAILED: "+test.name() + ": " + f.getMessage());
			return true;
		}
		catch (Throwable t)
		{
			System.out.println("FAILED: [UNKNOWN ERROR]: "+test.name() + ": " + t.getMessage());
			return true;
		}
	}
}
