package tw.jiangsir.Utils.Tools;

import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import tw.jiangsir.ZeroJiaowu.Objects.Course;

public class CSVReaderTool {

	public static ArrayList<Course> readCourse(String csv) {

		// CSV file header
		final String[] FILE_HEADER_MAPPING = {"firstName", "lastName", "gender", "age"};

		// Student attributes
		final String STUDENT_ID = "id";
		final String STUDENT_FNAME = "firstName";
		final String STUDENT_LNAME = "lastName";
		final String STUDENT_GENDER = "gender";
		final String STUDENT_AGE = "age";

		FileReader fileReader = null;

		CSVParser csvFileParser = null;

		// Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

		try {

			// Create a new list of student to be filled by CSV file data
			ArrayList<Course> students = new ArrayList();

			// initialize FileReader object
			// fileReader = new StringReader(csv);

			// initialize CSVParser object
			csvFileParser = new CSVParser(new StringReader(csv), csvFileFormat);

			// Get a list of CSV file records
			List<CSVRecord> csvRecords = csvFileParser.getRecords();

			// Read the CSV file records starting from the second record to skip
			// the header
			for (int i = 1; i < csvRecords.size(); i++) {
				CSVRecord record = csvRecords.get(i);
				// Create a new student object and fill his data

				Student student = new Student(Long.parseLong(record.get(STUDENT_ID)), record.get(STUDENT_FNAME),
						record.get(STUDENT_LNAME), record.get(STUDENT_GENDER),
						Integer.parseInt(record.get(STUDENT_AGE)));
				students.add(student);
			}

			// Print the new student list
			for (Student student : students) {
				System.out.println(student.toString());
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				csvFileParser.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader/csvFileParser !!!");
				e.printStackTrace();
			}
		}

	}

}
