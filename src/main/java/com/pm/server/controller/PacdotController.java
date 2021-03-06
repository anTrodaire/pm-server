package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.response.LocationResponse;
import com.pm.server.response.PacdotCountResponse;
import com.pm.server.response.PacdotResponse;
import com.pm.server.response.PacdotUneatenResponse;
import com.pm.server.response.ScoreResponse;
import com.pm.server.utils.JsonUtils;

@RestController
@RequestMapping("/pacdots")
public class PacdotController {

	@Autowired
	private PacdotRegistry pacdotRegistry;

	private final static Logger log =
			LogManager.getLogger(PacdotController.class.getName());

	@RequestMapping(
			value="/score",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<ScoreResponse>
			getGameScore(HttpServletResponse response) {

		log.info("Mapped GET /pacdots/score");

		Integer score = 0;
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			if(pacdot.getEaten()) {
				if(pacdot.getPowerdot()) {
					score += 50;
				}
				else {
					score += 10;
				}
			}
		}

		log.info("Calculated score {}", score);

		ScoreResponse scoreResponse = new ScoreResponse();
		scoreResponse.setScore(score);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(scoreResponse);
	}

	@RequestMapping(
			value="/count",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<PacdotCountResponse>
			getPacdotCount(HttpServletResponse response) {

		log.info("Mapped GET /pacdots/count");

		PacdotCountResponse countResponse = new PacdotCountResponse();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			countResponse.incrementTotal();
			if(pacdot.getEaten()) {
				countResponse.incrementEaten();
			}
			else {
				countResponse.incrementUneaten();
				if(pacdot.getPowerdot()) {
					countResponse.incrementUneatenPowerdots();
				}
			}

		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(countResponse);
	}

	@RequestMapping(
			value="/uneaten",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PacdotUneatenResponse>>
			getUneatenPacdots(HttpServletResponse response) {

		log.info("Mapped GET /pacdots/uneaten");

		List<PacdotUneatenResponse> responseList = new ArrayList<PacdotUneatenResponse>();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			if(!pacdot.getEaten()) {
				PacdotUneatenResponse pacdotResponse = new PacdotUneatenResponse();

				LocationResponse locationResponse = new LocationResponse();
				locationResponse.setLatitude(pacdot.getLocation().getLatitude());
				locationResponse.setLongitude(pacdot.getLocation().getLongitude());
				pacdotResponse.setLocation(locationResponse);
				pacdotResponse.setPowerdot(pacdot.getPowerdot());

				responseList.add(pacdotResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(responseList);
		if(objectString != null) {
			log.trace("Returning uneaten pacdots: {}", objectString);
		}

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseList);
	}

	@RequestMapping(
			value="",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PacdotResponse>>
			getAllPacdots(HttpServletResponse response) {

		log.info("Mapped GET /pacdots");

		List<PacdotResponse> responseList = new ArrayList<PacdotResponse>();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			PacdotResponse pacdotResponse = new PacdotResponse();

			LocationResponse locationResponse = new LocationResponse();
			locationResponse.setLatitude(pacdot.getLocation().getLatitude());
			locationResponse.setLongitude(pacdot.getLocation().getLongitude());
			pacdotResponse.setLocation(locationResponse);

			pacdotResponse.setEaten(pacdot.getEaten());
			pacdotResponse.setPowerdot(pacdot.getPowerdot());

			responseList.add(pacdotResponse);
		}

		String objectString = JsonUtils.objectToJson(responseList);
		if(objectString != null) {
			log.trace("Returning pacdot details: {}", objectString);
		}

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseList);
	}

}
