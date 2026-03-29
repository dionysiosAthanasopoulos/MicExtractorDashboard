package MicGenAI.MicExtractor.dashboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


class FileChooser extends JFrame {

	private static final long serialVersionUID = 1L;

	private String filePath, fileContent;

	String getFilePath() { return filePath; }

	String getFileContent() { return fileContent; }

	FileChooser() { init(null, null); }

	FileChooser(String content, String extension) { init(content, extension); }

	private void init(String content, String extension) {

		filePath = null;

		fileContent = null;

		JFileChooser chooser = new JFileChooser(new File("."));

		if(content == null) {

			String title = "Choose XML file...";

			chooser.setDialogTitle(title);

			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			chooser.showOpenDialog(this);

			File file = chooser.getSelectedFile();

			if(file != null && file.getName().endsWith(".xml")) {

				filePath = file.getAbsolutePath();

				try{ fileContent = read(file); } catch(Exception ex) { ex.printStackTrace(); }
			}

			else if(file != null) JOptionPane.showMessageDialog(null, "The selected file does not end with '.xml'", title, JOptionPane.ERROR_MESSAGE);
		}

		else {

			String title = "Choose Folder...";

			chooser.setDialogTitle(title);

			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			chooser.setApproveButtonText("Save");

			chooser.showOpenDialog(this);

			File file = chooser.getSelectedFile();

			if(file != null && extension != null && file.getName().endsWith(extension) && content != null) {

				filePath = file.getAbsolutePath();

				try{ write(content, filePath); } catch(Exception ex) { ex.printStackTrace(); }
			}

			else if(file != null) {

				if(content != null) JOptionPane.showMessageDialog(null, "The selected file does not end with '" + extension + "'.", title, JOptionPane.ERROR_MESSAGE);

				else JOptionPane.showMessageDialog(null, "The content is NULL.", title, JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private String read(File file) throws Exception{

		FileInputStream fstream = new FileInputStream(file);

		DataInputStream in = new DataInputStream(fstream);

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		int ch;

		StringBuilder content = new StringBuilder();

		while((ch = br.read()) != -1) content.append((char)ch);

		br.close();

		in.close();

		fstream.close();

		return content.toString();
	}

	private void write(String text, String fileName) throws Exception{

    	Writer output = new BufferedWriter(new FileWriter(fileName));

    	output.write(text);

    	output.close();
    }
}