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
import java.util.*;

@Repository
public class GeneralServiceImpl implements GeneralService {

    private static final long twepoch = 1288834974657L;
    private static final long sequenceBits = 17;
    private static final long sequenceMax = 65536;
    private static volatile long lastTimestamp = -1L;
    private static volatile long sequence = 0L;
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public GeneralServiceImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public java.sql.Array createSqlArray(List<String> list) {
        java.sql.Array intArray = null;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("text", list.toArray());
        } catch (SQLException ignore) {
        }
        return intArray;
    }

    @Override
    public String[] checkForNull(Array checkArray) {
        String[] checkedString = {"unknown"};
        if (checkArray != null) {
            try {
                checkedString = (String[]) checkArray.getArray();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return checkedString;
    }

    @Override
    public List<String> convertBase64toImageAndChangeName(String[] base64array) {
        List<String> base64List = Arrays.asList(base64array);
        List<String> imageUrls = null;
        for (String base64 : base64List) {
            long now = System.currentTimeMillis();
            byte[] data = Base64.decodeBase64(base64);
            Long uniqueIds = generateLongId();
            try {
                String url = "./src/resources/images/" + uniqueIds.toString() + ".jpg";
                FileOutputStream imageOutFile = new FileOutputStream(
                        url);
                imageUrls.add(url);
                imageOutFile.write(data);

                imageOutFile.close();
            } catch (Exception e) {

            }

        }
        return imageUrls;

    }

    public Long generateLongId() {
        long timestamp = System.currentTimeMillis();
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) % sequenceMax;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        Long id = ((timestamp - twepoch) << sequenceBits) | sequence;
        return id;
    }

    public long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
