package pro.trousev.cleer;

import java.util.Collection;

import pro.trousev.cleer.Item.Tag;

/**
 * Этот класс представляет собой интерфейс, похожий на современные OLAP интерфейсы, ориентированный на работу с cleer, но с ограничениями
 * 
 * Измерениями являются теги, drill through делается по значению или факту наличия тега 
 * Единственный факт -- количество Item-ов в выборке.
 * 
 * Интерфейс спроектирован в предположении о "ленивой" реализации: пока не запрошены реальные данные, размер класса и затраты на его создание
 * и обслуживание должны быть минимальны.
 * @author doctor
 *
 */
public interface VirtualOlap 
{
	/**
	 * Возвращает OLAP-куб, отфильтрованный по заданному измерению. 
	 */
	public VirtualOlap drillThrough(Tag dimensionValue);
	
	/**
	 * Возвращает OLAP-куб, отфильтрованный по заданному измерению
	 */
	public VirtualOlap drillThrough(String name, String value);

	/**
	 * Возвращает список значений заданного измерения
	 */
	public Collection<String> getDimension(String name);
	/**
	 * Возвращает значение аггрегатора в заданном кубе -- целиком
	 */
	public int getValue();
	public int[] getValue(String dimension);
	/**
	 * Этот интерфейс должна использовать и вызывать его методы реализация Database. Только в этом случае гарантируется работа кубика.
	 * @author doctor
	 */
	public static interface DatabaseMonitor
	{
		public void itemInserted(Item item);
		public void itemUpdated(Item item);
		public void itemRemoved(Item item);
		public void databaseCleared();
	}
}
