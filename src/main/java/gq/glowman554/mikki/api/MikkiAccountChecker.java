package gq.glowman554.mikki.api;

import java.io.File;
import java.io.IOException;

import gq.glowman554.mikki.api.data.MikkiAccount;
import gq.glowman554.mikki.api.data.MikkiToken;
import gq.glowman554.mikki.utils.FileUtils;

public class MikkiAccountChecker
{
	private final Mikki mikki;
	private final String token_file = System.getProperty("user.home") + "/.mikki.token";

	private MikkiAccount acc;
	private String token;

	public MikkiAccountChecker(Mikki mikki)
	{
		this.mikki = mikki;
	}

	public String token() throws IOException, IllegalArgumentException, IllegalAccessException
	{
		if (token == null)
		{
			token = FileUtils.readFile(token_file);

			if (!mikki.check(token))
			{
				new File(token_file).delete();
				throw new IllegalStateException("Invalid token!");
			}
		}

		return token;
	}

	public String login(String username, String password) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		MikkiToken token = mikki.login(username, password);

		FileUtils.writeFile(token_file, token.token);

		return token.token;
	}

	public String create(String username, String password) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		MikkiToken token = mikki.create_account(username, password);

		FileUtils.writeFile(token_file, token.token);

		return token.token;
	}

	public String username() throws IllegalArgumentException, IllegalAccessException, IOException
	{
		return mikki.info(token()).username;
	}

	public void logout()
	{
		new File(token_file).delete();
		acc = null;
		token = null;
	}

	public void chpasswd(String new_password) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		mikki.chpasswd(token(), new_password);
	}

	public void delete() throws IOException, IllegalArgumentException, IllegalAccessException
	{
		mikki.delete_account(token());
		logout();
	}

	public boolean check() throws IllegalArgumentException, IllegalAccessException
	{
		try
		{
			token();
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	public boolean editor() throws IllegalArgumentException, IllegalAccessException, IOException
	{
		if (acc == null)
		{
			acc = mikki.info(token());
		}

		return acc.editor;
	}
}
