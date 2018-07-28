package com.max.jna.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCVUtil {
	
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static MinMaxLocResult findCoordinate(BufferedImage scrImage, BufferedImage cutImage) throws IOException{
		Mat srcMat = MatUttil.doBufferedImageToMat(scrImage);
		Mat cutMat = MatUttil.doBufferedImageToMat(cutImage);
		Mat result = Mat.zeros(srcMat.rows() - cutMat.rows() + 1, srcMat.cols() - cutMat.cols() + 1, CvType.CV_32FC1);
		Imgproc.matchTemplate(srcMat, cutMat, result, Imgproc.TM_SQDIFF_NORMED);
		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
		MinMaxLocResult mlr = Core.minMaxLoc(result);
		return mlr;
	}
	
}
