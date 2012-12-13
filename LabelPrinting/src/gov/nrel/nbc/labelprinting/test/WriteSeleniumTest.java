package gov.nrel.nbc.labelprinting.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class WriteSeleniumTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WriteSeleniumTest wst = new WriteSeleniumTest();
		try {
			wst.createFile();
		} catch (Exception e) {
			System.out.println("exception caught: "+e);
		}
	}

    /**
     * Public method to create file containing printer commands.
     * 
     * @return <String> file name
     * @throws <Exception>
     */
    public String createFile() throws Exception {
    	Date date = new Date();
    	long time = date.getTime();
    	String filename = File.separator + "tmp" + File.separator + "TrackerTest" + time + ".txt";
    	System.out.println("filename = "+filename);
    	File f = new File(filename);
    	if (!f.exists()) {
    		PrintWriter out
    		   = new PrintWriter(new BufferedWriter(new FileWriter(f)));
    		String html = getHtml();
    		out.write(html);
    		out.close();
    		f.setWritable(true);
    		f.setReadable(true);
    		f.setExecutable(true);
    	}
    	else throw new Exception("file not unique");
    	return filename;
    }
    
	private String getHtml() {
		StringBuffer html = new StringBuffer();
		String sampleId = "012810-";
		html.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
		html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
		html.append("<head profile=\"http://selenium-ide.openqa.org/profiles/test-case\">\n");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
		html.append("<link rel=\"selenium.base\" href=\"\" />\n");
		html.append("<title>Tracker2</title>\n");
		html.append("</head>\n");
		html.append("<body>\n");
		html.append("<table cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n");
		html.append("<thead>\n");
		html.append("<tr><td rowspan=\"1\" colspan=\"3\">Tracker2</td></tr>\n");
		html.append("</thead><tbody>\n");
		html.append("<tr>\n");
		html.append("	<td>open</td>\n");
		html.append("	<td>/tracker/</td>\n");
		html.append("	<td></td>\n");
		html.append("</tr>\n");
		for (int ctr=0;ctr<1000;ctr++) {
			sampleId = "012810-" + String.valueOf(ctr);
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//input[@type='text']</td>\n");
			html.append("	<td>"+sampleId+"</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleTrbNum</td>\n");
			html.append("	<td>3601</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleTrbPage</td>\n");
			html.append("	<td>11</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[8]/td[2]/input</td>\n");
			html.append("	<td>James Walter Albersheim</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleDescriptor</td>\n");
			html.append("	<td>"+sampleId+"</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("<td>type</td>\n");
			html.append("<td>sampleExternalId</td>\n");
			html.append("<td>"+sampleId+"</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[11]/td[2]/input</td>\n");
			html.append("	<td>James Albersheim</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[12]/td[2]/select</td>\n");
			html.append("	<td>label=Kramer (generic)</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleStatus</td>\n");
			html.append("	<td>label=Stored</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleStatus</td>\n");
			html.append("	<td>label=In Process</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[15]/td[2]/input</td>\n");
			html.append("	<td>corn stover</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleTreatment</td>\n");
			html.append("	<td>label=pretreated</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleFraction</td>\n");
			html.append("	<td>label=solid</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleFire</td>\n");
			html.append("	<td>label=1 - Above 200F</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleReactivity</td>\n");
			html.append("	<td>label=0 - Stable</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleSpecific</td>\n");
			html.append("	<td>label=W - water reactivity</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleSpecific</td>\n");
			html.append("	<td>label=</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>select</td>\n");
			html.append("	<td>sampleHealth</td>\n");
			html.append("	<td>label=1 - Slightly Hazardous</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleAmount</td>\n");
			html.append("	<td>50</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[24]/td[2]/input</td>\n");
			html.append("	<td>FTLB</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[25]/td[2]/input</td>\n");
			html.append("	<td>117</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[26]/td[2]/input</td>\n");
			html.append("	<td>06</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[27]/td[2]/input</td>\n");
			html.append("	<td>upper</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[28]/td[2]/input</td>\n");
			html.append("	<td>cooler</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[29]/td[2]/input</td>\n");
			html.append("	<td>plastic bag</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleStorageNotes</td>\n");
			html.append("	<td>none</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>sampleComments</td>\n");
			html.append("	<td>auto test#"+ctr+"</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>type</td>\n");
			html.append("	<td>//input[@type='file']</td>\n");
			html.append("	<td>C:\\Documents and Settings\\jalbersh\\My Documents\\FFAddSecurityException2.JPG</td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>click</td>\n");
			html.append("	<td>//button[@type='button']</td>\n");
			html.append("	<td></td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>click</td>\n");
			html.append("	<td>//td[@id='sampleContainer']/div/div/div[3]/table/tbody/tr[1]/td/form/table/tbody/tr/td[1]/table/tbody/tr[40]/td[1]/button</td>\n");
			html.append("	<td></td>\n");
			html.append("</tr>\n");
			html.append("<tr>\n");
			html.append("	<td>open</td>\n");
			html.append("	<td>/tracker/</td>\n");
			html.append("	<td></td>\n");
			html.append("</tr>\n");
		}		
		html.append("</tbody></table>\n");
		html.append("</body>\n");
		html.append("</html>\n");
		return html.toString();
	}
}
