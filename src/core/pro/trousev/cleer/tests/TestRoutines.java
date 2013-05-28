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
			System.out.println("[pro.trousev.cleer] [test] [database] FAILED: "+test.name()+". Expected exception throwed, but nothing happened");
			return false;
		}
		catch (Throwable t)
		{
			System.out.println("[pro.trousev.cleer] [test] [database] PASSED: "+test.name());
			return true;
		}
	}
	
	boolean expectExecution(Test test)
	{
		try
		{
			test.run();
			System.out.println("[pro.trousev.cleer] [test] [database] PASSED: "+test.name());
			return false;
		}
		catch (Failure f)
		{
			System.out.println("[pro.trousev.cleer] [test] [database] FAILED: "+test.name() + ": " + f.getMessage());
			return true;
		}
		catch (Throwable t)
		{
			System.out.println("[pro.trousev.cleer] [test] [database] FAILED: [UNKNOWN: "+t.getClass().toString()+"]: "+test.name() + ": " + t.getMessage());
			
			for(StackTraceElement ste: t.getStackTrace())
			{
				System.out.println("[pro.trousev.cleer] [test] [database]       "+ste.toString());
			}
			return true;
		}
	}
}
