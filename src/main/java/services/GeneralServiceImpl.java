package services;


import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class GeneralServiceImpl implements GeneralService {
    private static final long twepoch = 1288834974657L;
    private static final long sequenceBits = 17;
    private static final long sequenceMax = 65536;
    private static volatile long lastTimestamp = -1L;
    private static volatile long sequence = 0L;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public GeneralServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public java.sql.Array createSqlArray(List<String> list) {
        java.sql.Array intArray = null;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            System.out.println("Schema:  " + conn.getSchema());
            intArray = conn.createArrayOf("text", list.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return intArray;
    }

    @Override
    public String[] checkForNull(Array checkArray) {
        String[] checkedString = {"unknown"};
        try {
            if (checkArray != null) {
                checkedString = (String[]) checkArray.getArray();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return checkedString;
    }

    @Override
    public List<String> convertBase64toImageAndChangeName(String[] base64array) {
        List<String> base64List = Arrays.asList(base64array);
        List<String> imageUrls = new ArrayList<>();
        try {
            for (String base64 : base64List) {
                long now = System.currentTimeMillis();
                byte[] data = Base64.decodeBase64(base64.split(",")[1]);
                Long uniqueIds = generateLongId();
                Path destinationFile = Paths.get("./src/main/resources/static/images/", uniqueIds.toString() + ".jpg");
                Files.write(destinationFile, data);
                imageUrls.add("./src/main/resources/static/images/" + uniqueIds.toString() + ".jpg");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return imageUrls;
    }

    private Long generateLongId() throws Exception {
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

    private long tilNextMillis(long lastTimestamp) throws Exception {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
