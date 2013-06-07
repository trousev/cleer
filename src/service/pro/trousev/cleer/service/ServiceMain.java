package pro.trousev.cleer.service;
import java.io.File;
import pro.trousev.cleer.sys.EbookItem;
class ServiceMain
{
    public static void main(String[] argv)
    {
        EbookItem.Factory.createItem(new File("../example.fb2"));
    }
}
