package services;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GeneralServiceImpl implements GeneralService {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public GeneralServiceImpl( JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public java.sql.Array createSqlArray(List<String> list){
        java.sql.Array intArray = null;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("text", list.toArray());
        } catch (SQLException ignore) {
        }
        return intArray;
    }

    @Override
    public String[] checkForNull(Array checkArray) {
        String[] checkedString={"unknown"} ;
        if (checkArray != null){
            try {
                checkedString = (String[]) checkArray.getArray();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return checkedString;
    }

    @Override
    public void convertBase64toImageAndChangeName(String base64) {
        byte[] data = Base64.decodeBase64(base64);

        try {
            FileOutputStream imageOutFile = new FileOutputStream(
                    "/Users/jeeva/Pictures/wallpapers/water-drop-after-convert.jpg");

            imageOutFile.write(data);

            imageOutFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
