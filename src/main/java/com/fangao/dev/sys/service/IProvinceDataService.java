package com.fangao.dev.sys.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface IProvinceDataService {
    boolean importExcel(String path,String fileName) throws IOException,ParseException;
}
