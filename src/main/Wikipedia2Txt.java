package main;

import help.Util_Numerical;
import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Wikipedia2Txt {
	
		//author --- Harinder
	private static int MAX_SENTENCES_TO_EXTRACT;
	private static String OUT_FILE;
	
	/**
	 * @param args
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws IOException, SAXException {
		
		Util_Numerical.init();
				
		//String dumpfile = "/data/src/sphinx/sphinxdata/wikipedia/enwiki-latest-pages-articles.xml";
		//String dumpfile = "/NumericExtraction/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000012664p000014909-1533.xml";
		List<String> listFiles = new ArrayList<String>();
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000010001p000012663-1420.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000012664p000014909-1533.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-563.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000018760p000020412-129.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000022414p000024434-1085.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000010001p000012663-817.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000022414p000024434-1512.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-305.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000022414p000024434-1053.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000012664p000014909-1510.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000010001p000012663-816.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000012664p000014909-1433.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000016628p000018758-1004.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000024435p000025000-421.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000018760p000020412-558.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-318.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000012664p000014909-475.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-730.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000010001p000012663-461.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-520.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000022414p000024434-481.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000018760p000020412-924.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000014910p000016625-871.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000022414p000024434-778.xml");
		listFiles.add("/notogit_wikisplit2/enwiki-20111201-pages-meta-history2-xml-p000010001p000012663-1602.xml");
		
		IArticleFilter handler = new ArticleFilter();
		MAX_SENTENCES_TO_EXTRACT = Integer.valueOf(args[0]);
		OUT_FILE = args[2];
        WikiXMLParser wxp = new WikiXMLParser(args[1], handler);

		wxp.parse();
		System.out.println("-------------Done---------------");
	}
	
	static Set<String> numericSentences = new HashSet<String>();
	
    /**
     * Print title an content of all the wiki pages in the dump.
     * 
     */
	static class ArticleFilter implements IArticleFilter {

		final static Pattern regex = Pattern.compile("[A-Z][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]", 
				Pattern.CANON_EQ);
		
		// Convert to plain text
		WikiModel wikiModel = new WikiModel("${image}", "${title}");

		public void process(WikiArticle page, Siteinfo siteinfo) throws SAXException {
			
			//if(numericSentences.size()!=0 && numericSentences.size()%10 == 0)
			if(numericSentences.size() >= MAX_SENTENCES_TO_EXTRACT) finalWork();
			
			if (page != null && page.getText() != null && !page.getText().startsWith("#REDIRECT ")){
				processing(page);
			}
		}

		private void processing(WikiArticle page) {
			
			String wikiText = getWikiText(page);
			String plainStr = getPlainStr(wikiText);
			Matcher regexMatcher = regex.matcher(plainStr);
			
			while (regexMatcher.find())
			{
				// Get sentences with 6 or more words
				String sentence = regexMatcher.group();

				if (matchSpaces(sentence, 5)) {
					try {

						if(!sentence.matches(".*\\d.*")) continue; 
						if(!Util_Numerical.isReqNumber(sentence)) continue;
						//if(!isContainsMentionedEntities(sentence)) continue; //---numberRule specific
						
						numericSentences.add(sentence);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void finalWork() {
			File file = new File(OUT_FILE);
			PrintWriter pw = null;
			
			try {
				pw = new PrintWriter(file);
				
				for (String string : numericSentences) {
					pw.println(string);
				}
				
				pw.close();
			} catch (FileNotFoundException e) {
				System.exit(1);
			}
			
			System.out.println("--------------DONE-----------");
			System.exit(0);//TODO
		}

		private boolean matchSpaces(String sentence, int matches) {

			int c =0;
			for (int i=0; i< sentence.length(); i++) {
				if (sentence.charAt(i) == ' ') c++;
				if (c == matches) return true;
			}
			return false;
		}

		private String getWikiText(WikiArticle page) {
			return page.getText().
								replaceAll("[=]+[A-Za-z+\\s-]+[=]+", " ").
								replaceAll("\\{\\{[A-Za-z0-9+\\s-]+\\}\\}"," ").
								replaceAll("(?m)<ref>.+</ref>"," ").
								replaceAll("(?m)<ref name=\"[A-Za-z0-9\\s-]+\">.+</ref>"," ").
								replaceAll("<ref>"," <ref>");
		}
	
		private String getPlainStr(String wikiText) {
			return wikiModel.render(new PlainTextConverter(), wikiText).
				replaceAll("\\{\\{[A-Za-z+\\s-]+\\}\\}"," ");
		}

	}

}