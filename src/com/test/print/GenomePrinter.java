package com.test.print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.neat.ConnectionGene;
import com.neat.Genome;
import com.neat.NodeGene;
import com.neat.TYPE;

public class GenomePrinter {
	
	public static BufferedImage getImage(Genome genome) {
		HashMap<Integer, Point> nodeGenePositions = new HashMap<Integer, Point>();
		int nodeSize = 40;
		int connectionSizeBulb = 10;
		int width = 1024;
		int height = 512;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.cyan);
		int inputNodes = genome.inputCount;
		int outputNodes = genome.outputCount;

		int inCount = 0;
		int outCount = 0;

		for (NodeGene gene : genome.getNodeGenes().values()) {

			double x = (gene.getX()*(width-nodeSize) + nodeSize/2);
			double y = gene.getY()*height;

			if(gene.getType() == TYPE.INPUT){
				y = (height/(inputNodes+1)) * ++inCount;
			}
			else if(gene.getType() == TYPE.OUTPUT){
				y = (height/(outputNodes+1)) * ++outCount;
			}

			
			g2d.setStroke(new BasicStroke(9));
			g.drawOval((int)(x-nodeSize/2), (int)(y-nodeSize/2), nodeSize, nodeSize);
			nodeGenePositions.put(gene.getId(), new Point((int)x,(int)y));
		}
		
		g.setColor(Color.WHITE);
		for (ConnectionGene gene : genome.getConnectionGenes().values()) {
			if (!gene.isEnabled()){
				continue;
			}
			Point inNode = nodeGenePositions.get(gene.getFromNodeId());
			Point outNode = nodeGenePositions.get(gene.getToNodeId());
			
			Point lineVector = new Point((int)((outNode.x - inNode.x) * 0.95f), (int)((outNode.y - inNode.y) * 0.95f));
			
			g2d.setStroke(new BasicStroke(2));
			g.drawLine(inNode.x, inNode.y, inNode.x+lineVector.x, inNode.y+lineVector.y);
			g.fillRect(inNode.x+lineVector.x-connectionSizeBulb/2, inNode.y+lineVector.y-connectionSizeBulb/2, connectionSizeBulb, connectionSizeBulb);
			g.drawString(String.format("%.2f", gene.getWeight()), (int)(inNode.x+lineVector.x*0.25f+5), (int)(inNode.y+lineVector.y*0.25f));
		}
		
		g.setColor(Color.RED);
		for (NodeGene nodeGene : genome.getNodeGenes().values()) {
			Point p = nodeGenePositions.get(nodeGene.getId());
			g.drawString(""+nodeGene.getId(), p.x-10, p.y);
		}
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, width, height);
		
		return image;
		
	}

	public static void printGenome(Genome genome, String path){

		BufferedImage image = getImage(genome);
		try {
			ImageIO.write(image, "PNG", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static int countNodesByType(Genome genome, TYPE type) {
		int c = 0;
		for (NodeGene node : genome.getNodeGenes().values()) {
			if (node.getType() == type) {
				c++;
			}
		}
		return c;
	}

}
