package ClientThings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shared.ImageOperation;
import shared.Join;
import shared.Result;
//sadasderr
public class LogoFinderWindow {
	private JFrame frame;

	private JTextField imagens;   

	private Client client;

	private DefaultListModel<String> listaImagens = new DefaultListModel<>();
	private DefaultListModel<String> listaWorkers = new DefaultListModel<>();

	private JList<String> TipoDeProcura;

	private List<Result> arrResults;

	public LogoFinderWindow(String title) throws IOException {
		frame = new JFrame(title);
		frame.setLocation(100, 75);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addFrameContent();
		frame.pack();
	}

	private void addFrameContent() throws IOException {
		frame.setLayout(new BorderLayout());
		JPanel painelSul = new JPanel();
		painelSul.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		if (true) {
			// natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}
		JButton pasta = new JButton("Pasta");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;

		imagens = new JTextField("C:\\Users\\henri\\OneDrive\\Ambiente de Trabalho\\imgTest");

		pasta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();

					imagens.setText(selectedFile.getAbsolutePath());
				}
			}
		});

		painelSul.add(pasta, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		painelSul.add(imagens, c);

		JButton imagem = new JButton("Imagem");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 1;

		JTextField logo = new JTextField("C:\\Users\\henri\\OneDrive\\Ambiente de Trabalho\\Superman.png");

		imagem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					logo.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		painelSul.add(imagem, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		painelSul.add(logo, c);

		JButton procura = new JButton("Procura");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 2;
		procura.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String path = imagens.getText();
				String pathLogo = logo.getText();

				if (path.endsWith(".png")) {
					throw new IllegalArgumentException("Por favor, selecione primeiro a pasta com as imagens.");
				}
				if (!pathLogo.endsWith("png")) {
					throw new IllegalArgumentException("Por favor, selecione um ficheiro .png.");
				}

				try {

					ArrayList<BufferedImage> arrBuff = new ArrayList<BufferedImage>();

					ArrayList<String> listaProcura = (ArrayList<String>) TipoDeProcura.getSelectedValuesList();
					ArrayList<byte[]> arrByte = new ArrayList<byte[]>();
					ArrayList<String> arrNames = new ArrayList<String>();
					byte[] logoByte;

					File file = new File(path);

					File[] listOfFiles = file.listFiles();
					for (File f : listOfFiles) {
						if (f != null && f.getName().toLowerCase().endsWith(".png") && f.isFile()) {
							arrBuff.add(ImageIO.read(f));
							arrNames.add(f.getAbsolutePath());
						}
					}
					for (int i = 0; i < arrBuff.size(); i++) {
						arrByte.add(ImageOperation.imageToByteArray(arrBuff.get(i)));

					}

					BufferedImage logo = ImageIO.read(new File(pathLogo));
					logoByte = ImageOperation.imageToByteArray(logo);

					Join join = new Join(arrByte, logoByte, listaProcura, arrNames);

					System.out.println("Tipos de Procura Selecionados: " + join.getProcura());

					client.send(join);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		painelSul.add(procura, c);

		frame.add(painelSul, BorderLayout.SOUTH);

		JLabel labelCentro = new JLabel();
		JScrollPane centro = new JScrollPane(labelCentro);

		JList<String> nomeDasFotos = new JList<String>(listaImagens);

		listaImagens.addElement("Images Names");

		nomeDasFotos.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int indexImagemSelecionada = nomeDasFotos.getSelectedIndex();

				if (indexImagemSelecionada != -1) {
					try {
						String imagemSelecionada = listaImagens.get(indexImagemSelecionada);
						String complete = (imagens.getText() + "\\" + imagemSelecionada);

						BufferedImage imagem = ImageIO.read(new File(complete));
						Graphics2D g2d = imagem.createGraphics();
						g2d.setColor(Color.RED);

						for (Result result : arrResults) {
							if (result.getImagePath().equals(complete)) {
								for (Rectangle rectangle : result.getMatches()) {
									g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
								}
							}
						}

						g2d.dispose();

						labelCentro.setIcon(new ImageIcon(imagem));

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		frame.add(nomeDasFotos, BorderLayout.EAST);

		TipoDeProcura = new JList<String>(listaWorkers);

		frame.add(TipoDeProcura, BorderLayout.WEST);

		frame.add(centro, BorderLayout.CENTER);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();

				int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit LogoFinder App?",
						"Exit Application", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION)
					client.close();

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});

	}

	// para abrir a janela (torna-la visivel)
	public void open(int width, int height) {
		frame.setSize(width, height);
		frame.setVisible(true);
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setResults(List<Result> arrResults) {
		listaImagens.clear();

		this.arrResults = arrResults;

		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					for (Result result : arrResults) {
						String s = result.getImagePath();
						int t = s.lastIndexOf("\\");
						String p = s.substring(t+1);
						
						
						
						if (!listaImagens.contains(p)) {
							listaImagens.addElement(p);
						}
					}

				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setWorkers(List<String> arrWorkers) {
		listaWorkers.clear();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					for (String s : arrWorkers) {
						if (!listaWorkers.contains(s)) {
							listaWorkers.addElement(s);

						}
					}
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// public static void main(String[] args) throws IOException {
	// LogoFinderWindow window = new LogoFinderWindow("LogoFinder App");
	// window.open(750, 500);
	// }

}
