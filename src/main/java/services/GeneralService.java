package services;



import java.sql.Array;
import java.util.List;

public interface GeneralService {


    java.sql.Array createSqlArray(List<String> list);
    String[] checkForNull(Array checkArray);
}
