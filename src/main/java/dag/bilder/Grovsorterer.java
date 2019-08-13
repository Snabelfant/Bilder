package dag.bilder;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Dag on 11.12.2016.
 */
public class Grovsorterer extends Panel {
    private final static File DIR = new File("C:\\Bilder ryddet\\Unike");
    private final static File JADIR = new File("C:\\Bilder ryddet\\Unike\\behold");
    private final static File NEIDIR = new File("C:\\Bilder ryddet\\Unike\\kast");
    private final static File VILDEDIR = new File("C:\\Bilder ryddet\\Unike\\Til Vilde");
    Bilde[] bilder;
    Bildepanel bp0;
    Bildepanel bp1;
    Bildepanel bp2;
    int bildeNaa = 0;
    JPanel ytre;

    public Grovsorterer() {
        File[] files = DIR.listFiles(pathname -> !pathname.isDirectory());
        bilder = new Bilde[files.length];
        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        for (int i = 0; i < files.length; i++) {
            bilder[i] = new Bilde((i + 1) + " av " + files.length, files[i]);
        }

        System.out.println("Filer=" + files.length);
        JADIR.mkdirs();
        NEIDIR.mkdirs();
        VILDEDIR.mkdirs();
    }

    public static void main(String args[]) throws Exception {
        Grovsorterer grovsorterer = new Grovsorterer();
        SwingUtilities.invokeLater(() -> grovsorterer.init());
    }

    private void init() {
        JFrame frame = new JFrame("Grovsortering");
        ytre = new JPanel();
        frame.getContentPane().add(ytre);

        ytre.setLayout(new BoxLayout(ytre, BoxLayout.X_AXIS));
        ytre.add(bp0 = new Bildepanel(0));
        ytre.add(bp1 = new Bildepanel(1));
        ytre.add(bp2 = new Bildepanel(2));

        bp1.addKeyListener(new KeyListener());
        bp1.setFocusable(true);
        bp1.requestFocusInWindow();

        int width = 1700;
        int heigth = 1000;
        frame.setSize(width, heigth);
        frame.setVisible(true);
        vis(bildeNaa);
    }

    private void vis(int i) {
        if (i < bilder.length - 1) {
            bp0.setBilde(bilder[i + 1]);
        }
        bp1.setBilde(bilder[i]);
        if (i > 0) {
            bp2.setBilde(bilder[i - 1]);
        }

        if (i > 1) {
            bilder[i - 2].slettBilde();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ytre.repaint();
            }
        });
    }

    private static class ImagePanel extends JPanel {
        private BufferedImage image;

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                Dimension d = getSize();
                int heightToDisplay = image.getHeight();
                int widthToDisplay = image.getWidth();

                if (image.getHeight() > d.height) {
                    heightToDisplay = d.height;
                    double scaleFactor = ((double) d.height) / image.getHeight();
                    widthToDisplay = (int) (image.getWidth() * scaleFactor);
                }

                if (image.getWidth() > d.width) {
                    widthToDisplay = d.width;
                    double scaleFactor = ((double) d.width) / image.getWidth();
                    heightToDisplay = (int) (image.getHeight() * scaleFactor);
                }

                if (image.getWidth() < d.width) {
                    widthToDisplay = d.width;
                    double scaleFactor = ((double) d.width) / image.getWidth();
                    heightToDisplay = (int) (image.getHeight() * scaleFactor);
                }

                g.drawImage(image, 0, 0, widthToDisplay, heightToDisplay, null);
            }
        }
    }

    private class Bildepanel extends JPanel {
        ImagePanel imagePanel;
        private Bilde bilde;
        private JLabel bildenavn;
        private JLabel bildenr;
        private JLabel kameramodell;

        public Bildepanel(int pos) {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            if (pos == 1) {
                setBorder(new LineBorder(Color.RED, 10, true));
            }

            add(bildenr = new JLabel("(Bildenr)"));
            add(bildenavn = new JLabel("(Bildenavn"));
            add(kameramodell = new JLabel("(kameramodell"));
            add(imagePanel = new ImagePanel());

            if (pos == 1) {
                JButton behold;
                JButton slett;
                JButton tilVilde;
                Box box = Box.createHorizontalBox();
                add(box);
                box.add(behold = new JButton("*** Behold ***"));
                behold.setSize(300, 100);
                box.add(Box.createRigidArea(new Dimension(100, 10)));
                box.add(slett = new JButton("Slett"));
                slett.setSize(100, 50);
                box.add(Box.createRigidArea(new Dimension(100, 10)));
                box.add(tilVilde = new JButton("-> Vilde"));
                tilVilde.setSize(100, 50);

                behold.addActionListener(e -> {
                    beholdOgVisNeste();
                });

                slett.addActionListener(e -> {
                    slettOgVisNeste();
                });

                tilVilde.addActionListener(e -> {
                    tilVildeOgVisNeste();
                });
            }
        }

        public void setBilde(Bilde bilde) {
            this.bilde = bilde;
            this.imagePanel.setImage(bilde.getImage());
        }

        public void beholdOgVisNeste() {
            bilde.behold();
            vis(++bildeNaa);
        }

        public void slettOgVisNeste() {
            bilde.slett();
            vis(++bildeNaa);
        }

        public void tilVildeOgVisNeste() {
            bilde.tilVilde();
            vis(++bildeNaa);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bilde != null) {
                bildenr.setText(bilde.getSekvensnr());
                bildenavn.setText(bilde.getNavn() + " (" + bilde.getInfo() + ") " + (bilde.slettet ? "SLETTET" : bilde.tilVilde ? "-> VILDE" :""));
                kameramodell.setText(bilde.getKameramodell());
            }
        }
    }

    private class Bilde {
        private String sekvensnr;
        private File file;
        private BufferedImage image;
        private boolean slettet;
        private boolean tilVilde;
        private String kameramodell;

        public Bilde(String sekvensnr, File file) {
            this.sekvensnr = sekvensnr;
            this.file = file;
            image = null;
            slettet = false;
        }

        public String getNavn() {
            return file.getName();
        }

        public BufferedImage getImage() {
            if (image == null) {
                try {
                    image = ImageIO.read(file);

                } catch (IOException ie) {
                    System.out.println("Feil:" + ie.getMessage());
                }


                Metadata metadata = null;
                try {
                    metadata = ImageMetadataReader.readMetadata(file);
                    Directory directory = metadata.getDirectory(ExifDirectory.class);
                    if (directory != null) {
                        kameramodell = directory.getString(ExifDirectory.TAG_MAKE) + "/" + directory.getString(ExifDirectory.TAG_MODEL);
                    }
                } catch (ImageProcessingException e) {
                    System.out.println("IMR=" + e);
                }
            }
            return image;
        }

        public String getInfo() {
            return image == null ? "?" : image.getHeight() + "/" + image.getWidth();
        }


        public void behold() {
            file.renameTo(new File(JADIR, file.getName()));
        }

        public void slett() {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Kaste " + file.getAbsoluteFile() + "?", "Kaste bilde?", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                slettet = true;
                file.renameTo(new File(NEIDIR, file.getName()));
            }
        }

        public void tilVilde() {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Flytte " + file.getAbsoluteFile() + "til Vilde?", "Til Vilde?", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                tilVilde = true;
                file.renameTo(new File(VILDEDIR, file.getName()));
            }
        }

        public String getSekvensnr() {
            return sekvensnr;
        }

        public void slettBilde() {
            image = null;
        }

        public String getKameramodell() {
            return kameramodell;
        }
    }


    private class KeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char keyChar = Character.toUpperCase(e.getKeyChar());
            System.out.println("K=" + keyChar);
            if (keyChar == 'B') {
                bp1.beholdOgVisNeste();
            } else {
                if (keyChar == 'S') {
                    bp1.slettOgVisNeste();
                }
            }
        }
    }

}
