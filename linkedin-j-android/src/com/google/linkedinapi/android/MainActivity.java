package com.google.linkedinapi.android;


import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.Person;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	/**
	 * Consumer Key
	 */
	private static final String CONSUMER_KEY_OPTION = "6h4l551spy3t";

	/**
	 * Consumer Secret
	 */
	private static final String CONSUMER_SECRET_OPTION = "dhX6rR6eVawixAE9";

	/**
	 * Access Token
	 */
	private static final String ACCESS_TOKEN_OPTION = "8e571e3b-e66a-4fca-90ce-bd9372d9fc65";

	/**
	 * Access Token Secret
	 */
	private static final String ACCESS_TOKEN_SECRET_OPTION = "294f2f11-b9c6-41e2-90e8-5ac8d15d5e40";
	/**
	 * ID
	 */
	private static final String ID_OPTION = "id";

	/**
	 * Email
	 */
	private static final String SUBJECT_OPTION = "subject";

	/**
	 * URL
	 */
	private static final String MESSAGE_OPTION = "message";

	/**
	 * Name of the help command line option.
	 */
	private static final String HELP_OPTION = "help";
	private boolean authenticated;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		processLinkedIn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem login = menu.findItem(R.id.login);
		login.setTitle(((authenticated) ? R.string.logoff_menu : R.string.login_menu));
		MenuItem profile = menu.findItem(R.id.profile);
		profile.setEnabled(authenticated);
		MenuItem connections = menu.findItem(R.id.connections);
		connections.setEnabled(authenticated);
		MenuItem updates = menu.findItem(R.id.updates);
		updates.setEnabled(authenticated);
		MenuItem search = menu.findItem(R.id.search);
		search.setEnabled(authenticated);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.login:
			Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
			authenticated = !authenticated;
			break;

		case R.id.profile:
			Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
			break;

		case R.id.connections:
			Toast.makeText(this, "Connections", Toast.LENGTH_SHORT).show();
			break;

		case R.id.updates:
			Toast.makeText(this, "Updates", Toast.LENGTH_SHORT).show();
			break;

		case R.id.search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return true;
	}

	private void processLinkedIn(){
		/*final LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(CONSUMER_KEY_OPTION, CONSUMER_SECRET_OPTION);
		final LinkedInApiClient client = factory.createLinkedInApiClient(ACCESS_TOKEN_OPTION, ACCESS_TOKEN_SECRET_OPTION);

		//fetching connection for current users
		Connections connections = client.getConnectionsForCurrentUser();
		printResult(connections);*/
		final LinkedInOAuthService oauthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(CONSUMER_KEY_OPTION, CONSUMER_SECRET_OPTION);
		System.out.println("Fetching request token from LinkedIn...");
		LinkedInRequestToken requestToken = oauthService.getOAuthRequestToken();
		String authUrl = requestToken.getAuthorizationUrl();

		System.out.println("Request token: " + requestToken.getToken());
		System.out.println("Token secret: " + requestToken.getTokenSecret());
		System.out.println("Expiration time: " + requestToken.getExpirationTime());

		System.out.println("Now visit:\n" + authUrl
				+ "\n... and grant this app authorization");

		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String pin = "1234";
		/*try {
			pin = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		System.out.println("Fetching access token from LinkedIn...");

		/*LinkedInAccessToken accessToken = oauthService.getOAuthAccessToken(requestToken, pin);

        System.out.println("Access token: " + accessToken.getToken());
        System.out.println("Token secret: " + accessToken.getTokenSecret());*/
		final LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(CONSUMER_KEY_OPTION, CONSUMER_SECRET_OPTION);
		final LinkedInApiClient client = factory.createLinkedInApiClient(ACCESS_TOKEN_OPTION, ACCESS_TOKEN_SECRET_OPTION);

		//fetching connection for current users
		Connections connections = client.getConnectionsForCurrentUser();
		printResult(connections);

		System.out.println("Fetching profile for current user.");
		Person profile = client.getProfileForCurrentUser();
		printResult(profile);
		
		Options options = buildOptions();
        try {
            CommandLine line = new BasicParser().parse(options, new String[]{});
            if(line.hasOption(ID_OPTION)) {
                String idValue = line.getOptionValue(ID_OPTION);
                System.out.println("Sending message to users with ids:" + idValue);
                client.sendMessage(Arrays.asList("dharm.chunnu@gmail.com","sandeep.climax@gmail.com"), "Testing", "Testing App");
                  System.out.println("Your message has been sent. Check the LinkedIn site for confirmation.");
              } else {
                System.out.println("Sending message to current user.");
                /*client.sendMessage(Arrays.asList("dharm.chunnu@gmail.com","sandeep.climax@gmail.com"), "Testing", "Testing App");
                System.out.println("Your message has been sent. Check the LinkedIn site for confirmation.");*/
                client.sendMessage(Collections.singletonList("~"), "Testing Again", "Testing App Again");
                  System.out.println("Your message has been sent. Check the LinkedIn site for confirmation.");
              }
            //processCommandLine(line, options);
        } catch(ParseException exp ) {
            System.err.println(exp.getMessage());
            //printHelp(options);
        }
	}
	/**
	 * Print the result of API call.
	 */
	private void printResult(Connections connections) {
		System.out.println("================================");
		System.out.println("Total connections fetched:" + connections.getTotal());
		for (Person person : connections.getPersonList()) {
			System.out.println(person.getId() + ":" + person.getFirstName() + " " + person.getLastName() + ":" + person.getHeadline());
		}
	}
	/**
	 * Print the result of API call.
	 */
	private static void printResult(Person profile) {
		System.out.println("================================");
		System.out.println("Name:" + profile.getFirstName() + " " + profile.getLastName());
		System.out.println("Headline:" + profile.getHeadline());
		System.out.println("Summary:" + profile.getSummary());
		System.out.println("Industry:" + profile.getIndustry());
		System.out.println("Picture:" + profile.getPictureUrl());
	}
	/**
	 * Build command line options object.
	 */
	private static Options buildOptions() {

		Options opts = new Options();

		String helpMsg = "Print this message.";
		Option help = new Option(HELP_OPTION, helpMsg);
		opts.addOption(help);

		/*String consumerKeyMsg = CONSUMER_KEY_OPTION;
		OptionBuilder.withArgName("consumerKey");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(consumerKeyMsg);
		Option consumerKey = OptionBuilder.create(CONSUMER_KEY_OPTION);
		opts.addOption(consumerKey);

		String consumerSecretMsg = CONSUMER_SECRET_OPTION;
		OptionBuilder.withArgName("consumerSecret");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(consumerSecretMsg);
		Option consumerSecret = OptionBuilder.create(CONSUMER_SECRET_OPTION);
		opts.addOption(consumerSecret);

		String accessTokenMsg = ACCESS_TOKEN_OPTION;
		OptionBuilder.withArgName("accessToken");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(accessTokenMsg);
		Option accessToken = OptionBuilder.create(ACCESS_TOKEN_OPTION);
		opts.addOption(accessToken);

		String tokenSecretMsg = ACCESS_TOKEN_SECRET_OPTION;
		OptionBuilder.withArgName("accessTokenSecret");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(tokenSecretMsg);
		Option accessTokenSecret = OptionBuilder.create(ACCESS_TOKEN_SECRET_OPTION);
		opts.addOption(accessTokenSecret);*/

		String idMsg = "dharm.chunnu@gmail.com,sandeep.climax@gmail.com";
		OptionBuilder.withArgName("ids");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(idMsg);
		Option id = OptionBuilder.create(ID_OPTION);
		opts.addOption(id);

		String subjectMsg = "Testing";
		OptionBuilder.withArgName("subject");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(subjectMsg);
		Option subject = OptionBuilder.create(SUBJECT_OPTION);
		opts.addOption(subject);

		String messageMsg = "Testing app";
		OptionBuilder.withArgName("message");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(messageMsg);
		Option message = OptionBuilder.create(MESSAGE_OPTION);
		opts.addOption(message);

		return opts;
	}
}