package pro.trousev.cleer.tests;

import org.jaudiotagger.Test;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.tests.TestRoutines.Failure;

/**
 * This class performs some tests on Database object.
 * 
 * @author Alexander Trousevich
 *
 */
public class TestDatabase extends TestRoutines implements CleerTest {
	private Database _db = null;
	public TestDatabase(Database db)
	{
		_db = db;
	}
	@Override
	public void run() {
		final String SECTNE = "SECT0";
		final String SECT1 = "SECT1";
		final String SECT2 = "SECT2";
		final String SECT3 = "SECT2";
		final String OBJ1 = "OBJ1";
		final String OBJ2 = "OBJ2";
		final String OBJ3 = "OBJ3";
		final String KEY1 = "KEY KEY1";
		final String KEY2 = "KEY KEY2";
		final String KEY3 = "KEY KEY3";
		
		expectException(new Test() {
			
			@Override
			public void run() throws Throwable {
				_db.store(SECTNE, OBJ1, KEY1);
			}
			
			@Override
			public String name() {
				return "Inability to store into non-existing section";
			}
		});
		
		expectExecution(new Test() {
			@Override
			public void run() throws Failure, Throwable {
				_db.clear_section(SECT1);
				_db.clear_section(SECT1);
				_db.declare_section(SECT1);
				_db.store(SECT1, OBJ1, KEY1);
				_db.store(SECT1, OBJ2, KEY2);
				_db.store(SECT1, OBJ3, KEY3);
			}
			@Override
			public String name() {
				return "Section creation and object placements";
			}
		});
		
		expectExecution(new Test() {
			@Override
			public void run() throws Failure, Throwable {
				expect(_db.search(SECT1, "KEY").size() == 3, "Number of stored objects is illegal");
				expect(_db.search(SECT1, "KEY1").size() == 1, "Number of stored objects is illegal");
				expect(_db.search(SECT1, "KEY2").size() == 1, "Number of stored objects is illegal");
				expect(_db.search(SECT1, "KEY3").size() == 1, "Number of stored objects is illegal");
				expect(_db.search(SECT1, "KEY1").get(0).contents().equals(OBJ1),"Stored and retreived object corrupts");
				expect(_db.search(SECT1, "KEY2").get(0).contents().equals(OBJ2),"Stored and retreived object corrupts");
				expect(_db.search(SECT1, "KEY3").get(0).contents().equals(OBJ3),"Stored and retreived object corrupts");
			}
			@Override
			public String name() {
				return "Basic search routines";
			}
		});
		
		expectExecution(new Test() {
			@Override
			public void run() throws Failure, Throwable {
				_db.store(SECT1, OBJ1, KEY1);
				_db.store(SECT1, OBJ1, KEY1);
				expect(_db.search(SECT1,KEY1).size() == 3, "Can't store double-object");
				expect(_db.search(SECT1,"").size() == 5, "Problems");
				_db.remove(SECT1,_db.search(SECT1, KEY1).get(1));
				expect(_db.search(SECT1,KEY1).size() == 2, "Not only one object removed");
				expect(_db.search(SECT1,"").size() == 4, "Not only one object removed[2]");
				for(DatabaseObject dbo: _db.search(SECT1, KEY1))
				{
					_db.remove(SECT1, dbo);
				}
				expect(_db.search(SECT1,KEY1).size() == 0, "DB was not cleared");
				expect(_db.search(SECT1,"").size() == 2, "DB was not cleared[2]");
				_db.store(SECT1, OBJ1, KEY1);
			}
			@Override
			public String name() {
				return "double-object insertion & deletion";
			}
		});
		
		expectExecution(new Test() {
			@Override
			public void run() throws Failure, Throwable {
				DatabaseObject o = _db.search(SECT1, KEY1).get(0);
				o.update_contents("OBJECT");
				expect(o.contents().equals("OBJECT"),"Object don't renamed");
				expect(_db.search(SECT1, KEY1).get(0).contents().equals("OBJECT"),"Renamed object don't updated in database");
				
				o.update_search("search");
				expect(o.search().equals("search"),"Object's search string don't changed");
				expect(_db.search(SECT1, "search").get(0).search().equals("search"),"Object's search string don't changed in DB");
				expect(_db.search(SECT1, KEY1).size() == 0, "Object still searchable with old key");
				
				o.update(OBJ1, KEY1);
				expect(_db.search(SECT1, KEY1).get(0).contents().equals(OBJ1),"Back-change did not passed");
				expect(_db.search(SECT1, KEY1).size() == 1, "Back-change search did not passed");
			}
			
			@Override
			public String name() {
				return "Object renaming routines";
			}
		});
		
		expectExecution(new Test() {
			
			@Override
			public void run() throws Failure, Throwable {
				_db.clear_section(SECT2);
				_db.declare_section(SECT2);
				expect(_db.search(SECT2,"").size() == 0, "New section have items");
				_db.begin();
				_db.store(SECT2, "00","0");
				_db.store(SECT2, "11","1");
				_db.store(SECT2, "22","2");
				expect(_db.search(SECT2,"").size() == 3, "In-transaction query does not return inserted elements");
				expect(_db.search(SECT2,"0").get(0).contents().equals("00"), "In-transaction query does not return inserted elements");
				_db.rollback();
				expect(_db.search(SECT2,"").size() == 0, "Rollbacked transaction don't rollback.");
				
				_db.begin();
				_db.store(SECT2, "000", "0");
				_db.store(SECT2, "WAAAGH", "0");
				_db.commit();
				expect(_db.search(SECT2,"").size() == 2, "Commited transaction don't exists");
			}
			
			@Override
			public String name() {
				return "Transactions testing";
			}
		});
	}
	
}
