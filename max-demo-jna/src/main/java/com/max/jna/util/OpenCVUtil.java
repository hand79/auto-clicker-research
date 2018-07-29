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
	/*
		匹配算法：
		TM_SQDIFF 平方差匹配法：該方法採用平方差來進行匹配；最好的匹配值為0；匹配越差，匹配值越大。
		TM_CCORR 相關匹配法：該方法採用乘法操作；數值越大表明匹配程度越好。
		TM_CCOEFF 相關係數匹配法：1表示完美的匹配；-1表示最差的匹配。
		TM_SQDIFF_NORMED 歸一化平方差匹配法。
		TM_CCORR_NORMED 歸一化相關匹配法。
		TM_CCOEFF_NORMED 歸一化相關係數匹配法。
	
		-- TmSqdiffNormed
		. find
		coordinate.minVal:0.0021723953541368246
		coordinate .minVal:1.0
		. not
		coordinate.minVal:0.14602431654930115
		coordinate .minVal:1.0

		-- TmCcoeffNormed
		. find
		coordinate.minVal:-0.25615280866622925
		coordinate.maxVal:0.9928069114685059
		. not
		coordinate.minVal:-0.2561522126197815
		coordinate.maxVal:0.3023257553577423
	 */
	
	public static MinMaxLocResult findCoordinateByTmSqdiffNormed(BufferedImage scrImage, BufferedImage cutImage) throws IOException{
		// Matrix
		// alpha, blue, green,red
		Mat srcMat = MatUttil.doBufferedImageToMat(scrImage);
		Mat cutMat = MatUttil.doBufferedImageToMat(cutImage);
		// https://docs.opencv.org/master/df/dfb/group__imgproc__object.html#ga586ebfb0a7fb604b35a23d85391329be
		Mat result = Mat.zeros(srcMat.rows() - cutMat.rows() + 1, srcMat.cols() - cutMat.cols() + 1, CvType.CV_32FC1);
		// 	Mask of searched template, only the TM_SQDIFF and TM_CCORR_NORMED methods are supported. 
		Imgproc.matchTemplate(srcMat, cutMat, result, Imgproc.TM_SQDIFF_NORMED);
		// if you use thresholdMatch, close the method 
		// http://answers.opencv.org/question/77543/how-to-reduce-false-detection-of-template-matching/
//		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
		MinMaxLocResult mlr = Core.minMaxLoc(result);
		return mlr;
	}
	
	public static MinMaxLocResult findCoordinateByTmCcoeffNormed(BufferedImage scrImage, BufferedImage cutImage) throws IOException{
		// Matrix
		// alpha, blue, green,red
		Mat srcMat = MatUttil.doBufferedImageToMat(scrImage);
		Mat cutMat = MatUttil.doBufferedImageToMat(cutImage);
		// https://docs.opencv.org/master/df/dfb/group__imgproc__object.html#ga586ebfb0a7fb604b35a23d85391329be
		Mat result = Mat.zeros(srcMat.rows() - cutMat.rows() + 1, srcMat.cols() - cutMat.cols() + 1, CvType.CV_32FC1);
		// 	Mask of searched template, only the TM_SQDIFF and TM_CCORR_NORMED methods are supported. 
		Imgproc.matchTemplate(srcMat, cutMat, result, Imgproc.TM_CCOEFF_NORMED);
		// if you use thresholdMatch, close the method
		// http://answers.opencv.org/question/77543/how-to-reduce-false-detection-of-template-matching/
//		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
		MinMaxLocResult mlr = Core.minMaxLoc(result);
		return mlr;
	}
	
	public static boolean isMatchTmSqdiffNormed(MinMaxLocResult mlr, double thresholdMatch) throws IOException{
		// minVal's match value  in 0 ~ 0.014  
		// current use 0.05 maybe is ok
		if( mlr.minVal >= 0.0 && mlr.minVal <= thresholdMatch){
			return true;
		}
		return false;
	}
	
	public static boolean isMatchTmCcoeffNormed(MinMaxLocResult mlr, double thresholdMatch) throws IOException{
		 // maxVal's match value in 0.7 ~ 1.0
		if(mlr.maxVal >= thresholdMatch){
			return true;
		}
		return false;
	}
	
}
