package com.yiij.base;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.CRC32;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;

import com.yiij.base.interfaces.IContext;

public abstract class Application extends Module
{
	private String _id;
	private String _name = "My Application";
	private String _basePath;
	private String _runtimePath;
	private String _language;
	private String _sourceLanguage = "en_us";
	private String _charset = "UTF-8";
	private Map<String, String> _aliases = new Hashtable<String, String>();
	
	public Application(IContext context)
	{
		super(context, "", null);
		
		context.setApplication(this);
		
		registerCoreComponents();
	}

	/**
	 * Processes the request.
	 * This is the place where the actual request processing work is done.
	 * Derived classes should override this method.
	 */
	abstract public void processRequest() throws java.lang.Exception;
	
	/**
	 * Runs the application.
	 * This method loads static application components. Derived classes usually overrides this
	 * method to do more application-specific tasks.
	 * Remember to call the parent implementation so that static application components are loaded.
	 */
	public void run() throws ServletException, IOException
	{
		//if($this->hasEventHandler('onBeginRequest'))
			//$this->onBeginRequest(new CEvent($this));
		try
		{
			processRequest();
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (java.lang.Exception e) {
			throw new ServletException(e);
		}
		//if($this->hasEventHandler('onEndRequest'))
			//$this->onEndRequest(new CEvent($this));
	}

	/**
	 * Terminates the application.
	 * This method replaces PHP's exit() function by calling
	 * {@link onEndRequest} before exiting.
	 * @param integer $status exit status (value 0 means normal exit while other values mean abnormal exit).
	 * @param boolean $exit whether to exit the current request. This parameter has been available since version 1.1.5.
	 * It defaults to true, meaning the PHP's exit() function will be called at the end of this method.
	 */
	public void end(int status, boolean exit)
	{
		//if($this->hasEventHandler('onEndRequest'))
			//$this->onEndRequest(new CEvent($this));
		//if($exit)
			//exit($status);
	}
	
	/**
	 * Returns the unique identifier for the application.
	 * @return string the unique identifier for the application.
	 */
	public String getId()
	{
		if(_id!=null)
			return _id;
		else
		{
			CRC32 cs = new CRC32();
			cs.update((getBasePath()+_name).getBytes());
			return _id=String.format("%x", cs.getValue());
		}
	}

	/**
	 * Sets the unique identifier for the application.
	 * @param string $id the unique identifier for the application.
	 */
	public void setId(String id)
	{
		_id=id;
	}
	
	/**
	 * Returns the root path of the application.
	 * @return the root directory of the application. Defaults to 'protected'.
	 */
	public String getBasePath()
	{
		return _basePath;
	}

	/**
	 * Sets the root directory of the application.
	 * This method can only be invoked at the begin of the constructor.
	 * @param path the root directory of the application.
	 * @throws Exception if the directory does not exist.
	 */
	public void setBasePath(String path)
	{
		_basePath = path;
		
		/*
		if(($this->_basePath=realpath($path))===false || !is_dir($this->_basePath))
			throw new CException(Yii::t('yii','Application base path "{path}" is not a valid directory.',
				array('{path}'=>$path)));
        $this->_autoBasePathUrl = null;
        */
	}
	
	/**
	 * Returns the directory that stores runtime files.
	 * @return the directory that stores runtime files. Defaults to 'protected/runtime'.
	 */
	public String getRuntimePath()
	{
		return _runtimePath;
		/*
		if(_runtimePath!=null)
			return _runtimePath;
		else
		{
			setRuntimePath(getBasePath()+"/runtime");
			return _runtimePath;
		}
		*/
	}
	
	/**
	 * Sets the directory that stores runtime files.
	 * @param string $path the directory that stores runtime files.
	 * @throws CException if the directory does not exist or is not writable
	 */
	public void setRuntimePath(String path)
	{
		_runtimePath = path;
		
		/*
		if(($runtimePath=realpath($path))===false || !is_dir($runtimePath) || !is_writable($runtimePath))
			throw new CException(Yii::t('yii','Application runtime path "{path}" is not valid. Please make sure it is a directory writable by the Web server process.',
				array('{path}'=>$path)));
		$this->_runtimePath=$runtimePath;
		*/
	}
	
	/**
	 * Returns the language that the user is using and the application should be targeted to.
	 * @return string the language that the user is using and the application should be targeted to.
	 * Defaults to the {@link #getSourceLanguage() source language}.
	 */
	public String getLanguage()
	{
		return _language==null ? _sourceLanguage : _language;
	}

	/**
	 * Specifies which language the application is targeted to.
	 *
	 * This is the language that the application displays to end users.
	 * If set null, it uses the {@link #getSourceLanguage() source language}.
	 *
	 * Unless your application needs to support multiple languages, you should always
	 * set this language to null to maximize the application's performance.
	 * @param language the user language (e.g. 'en_US', 'zh_CN').
	 * If it is null, the {@link #getSourceLanguage()} will be used.
	 */
	public void setLanguage(String language)
	{
		_language=language;
	}

	/**
	 * @return the application name. Defaults to 'My Application'.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @param the application name. Defaults to 'My Application'.
	 */
	public void setName(String name)
	{
		_name=name;
	}
	
	
	/**
	 * @return the charset currently used for the application. Defaults to 'UTF-8'.
	 */
	public String getCharset()
	{
		return  _charset;
	}

	/**
	 * @param charset the charset to be used for the application. Defaults to 'UTF-8'.
	 */
	public void setCharset(String charset)
	{
		_charset=charset;
	}
	
	/**
	 * the language that the application is written in. This mainly refers to
	 * the language that the messages and view files are in. Defaults to 'en_us' (US English).
	 * @return
	 */
	public String getSourceLanguage()
	{
		return  _sourceLanguage;
	}

	/**
	 * the language that the application is written in. This mainly refers to
	 * the language that the messages and view files are in. Defaults to 'en_us' (US English).
	 * @param language
	 */
	public void setSourceLanguage(String language)
	{
		_sourceLanguage=language;
	}
	
	
	//public function findLocalizedFile($srcFile,$srcLanguage=null,$language=null)
	
	
	/**
	 * Registers the core application components.
	 * @see #setComponents()
	 */
	protected void registerCoreComponents()
	{
	}
	
	public String getPathOfAlias(String alias)
	{
		int pos;
		if (_aliases.containsKey(alias))
			return _aliases.get(alias);
		else if ((pos=alias.indexOf("."))!=-1)
		{
			String rootAlias = alias.substring(0, pos);
			if (_aliases.containsKey(rootAlias))
			{
				String apath = StringUtils.stripEnd(_aliases.get(rootAlias)+"/"+alias.substring(pos+1).replaceAll("\\.", "/"), "*/");
				_aliases.put(alias, apath);
				return apath;
			}
		}
		return null;
	}

	public void setPathOfAlias(String alias, String value)
	{
		if (value == null)
			_aliases.remove(alias);
		else
			_aliases.put(alias, StringUtils.stripEnd(value, "\\/"));
	}
	
}
