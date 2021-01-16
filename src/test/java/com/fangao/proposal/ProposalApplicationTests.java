package com.fangao.proposal;

import com.fangao.dev.sys.dto.JdcDTO;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProposalApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		// 模板路径和输出流
//		String templatePath = "./src/main/resources/excel/工作进展模板.xlsx";
//		OutputStream os = new FileOutputStream("out.xlsx");
		// 定义一个Map，往里面放入要在模板中显示数据
		Map<String, Object> model = new HashMap<String, Object>();
		List<JdcDTO> jdcDTOList=new ArrayList<>();
		JdcDTO jdcDTO=new JdcDTO();
		jdcDTO.setEventNum("F20190513009");
		jdcDTO.setName("事件名称");
		jdcDTO.setPetitionerName("崔天祥");
		jdcDTO.setEventPlaceName("地方名");
		jdcDTO.setEventContent("反应问题");
		jdcDTOList.add(jdcDTO);
		jdcDTOList.add(jdcDTO);

		model.put("progress", jdcDTOList);
//		model.put("id", 123);

		//调用之前写的工具类，传入模板路径，输出流，和装有数据Map
//		JxlsUtils.exportExcel(templatePath, os, model);
//		os.close();



		/**
		 * excel导出
		 * 1.获取数据集List 插入到map集合中
		 * 2.根据模板生成新的excel
		 * 3.将新生成的excel文件从浏览器输出
		 * 4.删除新生成的模板文件
		 */


		//加载excel模板文件
		File file = null;
		try {
			file = ResourceUtils.getFile("src/main/resources/excel/aaa.xlsx");
		} catch (FileNotFoundException e) {
//			log.error("template file path cannot be found");
		}

		//配置下载路径
		String path = "src/main/resources/excel/";
		createDir(new File(path));

		//根据模板生成新的excel
		File excelFile = createNewFile(model, file, path);

//		//浏览器端下载文件
//		downloadFile(response, excelFile);
//
//		//删除服务器生成文件
//		deleteFile(excelFile);
//		System.out.println("完成");


	}
	private File createNewFile(Map<String, Object> beans, File file, String path) {
		XLSTransformer transformer = new XLSTransformer();

		//可以写工具类来生成命名规则
		String name = "bbb.xlsx";
		File newFile = new File(path + name);


		try (InputStream in = new BufferedInputStream(new FileInputStream(file));
			 OutputStream out = new FileOutputStream(newFile)) {
			Workbook workbook = transformer.transformXLS(in, beans);
			workbook.write(out);
			out.flush();
			return newFile;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return newFile;
	}

	/**
	 * 将服务器新生成的excel从浏览器下载
	 *
	 * @param response
	 * @param excelFile
	 */
	private void downloadFile(HttpServletResponse response, File excelFile) {
		/* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
		response.setContentType("multipart/form-data");
		/* 设置文件头：最后一个参数是设置下载文件名 */
		response.setHeader("Content-Disposition", "attachment;filename=" + excelFile.getName());
		try (
				InputStream ins = new FileInputStream(excelFile);
				OutputStream os = response.getOutputStream()
		) {
			byte[] b = new byte[1024];
			int len;
			while ((len = ins.read(b)) > 0) {
				os.write(b, 0, len);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 浏览器下载完成之后删除服务器生成的文件
	 * 也可以设置定时任务去删除服务器文件
	 *
	 * @param excelFile
	 */
	private void deleteFile(File excelFile) {

		excelFile.delete();
	}

	//如果目录不存在创建目录 存在则不创建
	private void createDir(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
