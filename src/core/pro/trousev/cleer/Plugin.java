package pro.trousev.cleer;

import java.io.PrintStream;
import java.util.List;


public interface Plugin {
	public interface Interface
	{
		Library library();
		Database storage();
		Console console();
		Player player();
		Queue queue();
		ConsoleOutput output();
	}
	public interface Command extends Comparable<Plugin.Command>
	{
		/** 
		 * Должна возвращать имя команды
		 */
		public String name();
		/**
		 * Должен возвращать описание в одну строку / полную справку по команде
		 * @param is_short указывает, должно ли это быть 
		 * однострочное описание или полная документация
		 */
		public String help(boolean is_short);
		/**
		 * Самый Главный Метод. Вызывается, если пользователь таки ввел эту команду
		 * @param args Набор параметров команды 
		 * @param stdout Место, куда выводить output
		 * @param iface Доступный интерфейс плеера
		 * @return
		 */
		public boolean main(List<String> args, PrintStream stdout, Interface iface);
	}
	List<Command> commands();
}
