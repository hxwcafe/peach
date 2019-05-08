package com.example.peach;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.dao.CommodityMapper;
import com.example.peach.dao.UserMapper;
import com.example.peach.dao.UserVipMapper;
import com.example.peach.pojo.Album;
import com.example.peach.pojo.Orderpay;
import com.example.peach.pojo.User;
import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.*;
import com.example.peach.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialArray;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PeachoneApplicationTests {
    @Test
    public void demo() {

    }
}
