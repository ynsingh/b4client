package com.ehelpy.brihaspati4.Distributed_Search;
// Class to send query to Lucene library
import java.io.IOException; 
import org.apache.lucene.document.Document; 
import org.apache.lucene.queryParser.ParseException; 
import org.apache.lucene.search.ScoreDoc; 
import org.apache.lucene.search.TopDocs; 
public class LuceneTester 
// Declaration of variables to store data and indexes of files
{ 
	String indexDir = "dat\\index"; 
	String dataDir = "dat\\data"; 
	Indexer indexer; // Creating an object of Indexer Class
	Searcher searcher; //Creating an object of Indexer Class
        public static String result="";
	// Passing Query obtained from user	
	public static String main(String query) 
	{ 
                result="";
		LuceneTester tester; 
		try 
		{ 
			tester = new LuceneTester(); 
			tester.createIndex(); 
			tester.search(query); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		catch (ParseException e) 
		{ 
			e.printStackTrace(); 
		}
                
                return result;
	} 
	private void createIndex() throws IOException
	{ 
		indexer = new Indexer(indexDir); 
		int numIndexed; 
		long startTime = System.currentTimeMillis(); 
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter()); 
		long endTime = System.currentTimeMillis(); 
		indexer.close(); 
		System.out.println(numIndexed+" File indexed, time taken: " +(endTime-startTime)+" ms"); 
	} 
	private void search(String searchQuery) throws IOException, ParseException
	{ // To compute the start and end time of Search
		searcher = new Searcher(indexDir); 
		long startTime = System.currentTimeMillis(); 
		TopDocs hits = searcher.search(searchQuery); 
		long endTime = System.currentTimeMillis(); 
		System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime)); 
		for(ScoreDoc scoreDoc : hits.scoreDocs) 
		{ 
                    Document doc = searcher.getDocument(scoreDoc); 
                    
                    result+="File: " + doc.get(LuceneConstants.FILE_PATH)+"\n";
                } 
		searcher.close(); 
	} 
}