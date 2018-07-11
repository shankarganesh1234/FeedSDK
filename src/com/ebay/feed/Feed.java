package com.ebay.feed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import com.ebay.models.FeedRequest;
import com.ebay.models.FeedResponseFlag;
import com.feed.constants.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Feed {

	private OkHttpClient client = null;

	public Feed() {
		client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS).build();

	}

	/**
	 * 
	 * @param feedRequest
	 * @return
	 */
	public boolean get(FeedRequest feedRequest) {

		boolean result = true;

		if (feedRequest == null)
			return false;

		try {
			result = process(feedRequest);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 
	 * 
	 * @param feedRequest
	 * @return
	 * @throws IOException
	 */
	private boolean process(FeedRequest feedRequest) throws IOException {

		if (feedRequest.getToken() == null || feedRequest.getType() == null || feedRequest.getCategory_id() == null
				|| feedRequest.getSiteId() == null)
			return false;

		Request.Builder requestBuilder = new Request.Builder();

		Path pathToFile = Paths.get("/Users/shankarganesh/temp/" + feedRequest.getCategory_id() + "_"
				+ feedRequest.getSiteId() + "_" + feedRequest.getDate());
		Files.createDirectories(pathToFile.getParent());

		if (Files.exists(pathToFile)) {
			Files.delete(pathToFile);
		}

		Path path = Files.createFile(pathToFile);

		// generate static request
		requestBuilder = generateRequest(feedRequest, requestBuilder);

		// genearate dynamic header
		requestBuilder.addHeader("Range", "bytes=0-" + Constants.CHUNK_SIZE);

		// invoke request
		return invoker(requestBuilder, path, true);
	}

	/**
	 * 
	 * @param request
	 * @param path
	 * @param isStart
	 * @return
	 */
	private boolean invoker(Request.Builder requestBuilder, Path path, boolean isStart) {

		FeedResponseFlag responseFlag = invokeIteratively(requestBuilder.build(), path);

		if (responseFlag.getStatusCode() == 200) {
			System.out.println("all done...");
			return true;

		} else if (responseFlag.getStatusCode() == 206) {

			long requestRangeUpperLimit = Long.valueOf(requestBuilder.build().header("Range").split("-")[1]) + 1;
			long responseRangeUpperLimit = Long.valueOf(responseFlag.getContentRange().split("/")[1]);

			while (requestRangeUpperLimit <= responseRangeUpperLimit) {

				long newUpperLimit = requestRangeUpperLimit + Constants.CHUNK_SIZE;
				String val = "bytes=" + requestRangeUpperLimit + "-" + newUpperLimit;

				requestBuilder.removeHeader("Range");
				requestBuilder.addHeader("Range", val);

				responseFlag = invokeIteratively(requestBuilder.build(), path);

				if (responseFlag == null) {
					return false;
				}

				requestRangeUpperLimit = Long.valueOf(requestBuilder.build().header("Range").split("-")[1]) + 1;
				responseRangeUpperLimit = Long.valueOf(responseFlag.getContentRange().split("/")[1]);

			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param request
	 * @param path
	 * @return
	 */
	private FeedResponseFlag invokeIteratively(Request request, Path path) {

		FeedResponseFlag responseFlag = null;

		try (Response response = client.newCall(request).execute()) {

			InputStream is = response.body().byteStream();

			OutputStream outStream = new FileOutputStream(path.toString(), true);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
			outStream.close();
			is.close();

			responseFlag = new FeedResponseFlag(response.header("Content-Range"), response.code());

		} catch (Throwable t) {
			t.printStackTrace();
			responseFlag = new FeedResponseFlag(null, 400);
		}
		return responseFlag;
	}

	/**
	 * 
	 * @param feedRequest
	 * @return
	 */
	private String getFinalUrl(FeedRequest feedRequest) {

		String type = feedRequest.getType();
		StringBuilder bdr = new StringBuilder(Constants.FEED_API_BASE);
		switch (type) {

		case "item":

			bdr.append("item?feed_scope=");
			bdr.append(feedRequest.getFeed_scope());
			bdr.append("&category_id=");
			bdr.append(feedRequest.getCategory_id());
			bdr.append("&date=");
			bdr.append(feedRequest.getDate());
			return bdr.toString();

		default:
			return null;
		}
	}

	/**
	 * 
	 * @param feedRequest
	 * @param requestBuilder
	 * @return
	 */
	private Request.Builder generateRequest(FeedRequest feedRequest, Request.Builder requestBuilder) {
		// static headers
		requestBuilder.addHeader("X-EBAY-C-MARKETPLACE-ID", feedRequest.getSiteId());
		requestBuilder.addHeader("Content-type", "application/json");
		requestBuilder.addHeader("Accept", "application/json");

		// url
		requestBuilder.url(getFinalUrl(feedRequest));

		// token
		requestBuilder.addHeader("Authorization", feedRequest.getToken());
		return requestBuilder;
	}

}
