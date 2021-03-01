package com.happy3w.jasper.playground;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.renderers.Graphics2DRenderable;
import net.sf.jasperreports.renderers.Renderable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WaterMarkRenderer implements Renderable, Graphics2DRenderable {

    private String id = "myId";;
    private final String text;
    private final double rotateAngle;
    private final double step;

    public WaterMarkRenderer(String text, double rotateAngle, double step) {
        this.text = text;
        this.rotateAngle = rotateAngle;
        this.step = step;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }


    private Point2D.Double rotate(Point2D.Double point, double sina, double cosa) {
        double x = point.x;
        double y = point.y;
        return new Point2D.Double(x * cosa + y * sina, - x * sina + y * cosa);
    }
    
    @Override
    public void render(JasperReportsContext jasperReportsContext, Graphics2D g2, Rectangle2D rectangle) {
        double h = rectangle.getHeight();
        double w = rectangle.getWidth();

        double radian = rotateAngle * Math.PI / 180;
        double sinR = Math.sin(radian);
        double cosR = Math.cos(radian);

        List<Point2D.Double> rotatedRectPoints = new ArrayList<>();
        rotatedRectPoints.add(new Point2D.Double(0, 0));
        rotatedRectPoints.add(rotate(new Point2D.Double(w, 0), sinR, cosR));
        rotatedRectPoints.add(rotate(new Point2D.Double(w, h), sinR, cosR));
        rotatedRectPoints.add(rotate(new Point2D.Double(0, h), sinR, cosR));

        ValueRange xRange = calculateRange(Point2D::getX, rotatedRectPoints);
        ValueRange yRange = calculateRange(Point2D::getY, rotatedRectPoints);

        double targetWidth = xRange.getMax() - xRange.getMin();
        double targetHeight = yRange.getMax() - yRange.getMin();

        Point2D.Double newTopLeft = rotate(new Point2D.Double(xRange.getMin(), yRange.getMin()),
                Math.sin(-radian), Math.cos(-radian));

        g2.translate(newTopLeft.getX(), newTopLeft.getY());
        g2.rotate(radian);
        g2.setColor(new Color(255, 0, 0, 100));
        g2.setFont(new Font("Arial", Font.PLAIN, 20));

        g2.drawRect(0, 0, (int) targetWidth, (int) targetHeight);
        double offset = 0;
        for (double y = 0; y < targetHeight; y += step) {
            offset = step / 2 - offset;
            for (double x = 0; x < targetWidth; x += step) {
                g2.drawString(text, (float) (x + offset), (float)  y);
            }
        }
    }

    private ValueRange calculateRange(Function<Point2D, Double> valueGetter, List<? extends Point2D> points) {
        Point2D point1 = points.get(0);

        double min = valueGetter.apply(point1);
        double max = min;
        for (int i = 1; i < points.size(); i++) {
            Point2D p = points.get(i);
            double v = valueGetter.apply(p);
            if (v < min) {
                min = v;
            } else if (v > max) {
                max = v;
            }
        }
        return new ValueRange(min, max);
    }

    public static void addMark(JasperPrint jasperPrint, String text, double rotateAngle, double step) {
        WaterMarkRenderer waterMarkRenderer = new WaterMarkRenderer(text, rotateAngle, step);

        JRPrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
        image.setX(0);
        image.setY(0);
        image.setWidth(jasperPrint.getPageWidth());
        image.setHeight(jasperPrint.getPageHeight());
        image.setScaleImage(ScaleImageEnum.CLIP);
        image.setRenderer(waterMarkRenderer);

        for (JRPrintPage page : jasperPrint.getPages()) {
            page.addElement(image);
        }
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    private static class ValueRange {
        private double min;
        private double max;
    }
}
