package com.aleksey.vasiliev.theshunt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxBoxCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class MyShuntGame implements Screen {
	private Game parent;
	private AssetManager assets;

	private Music forest;
	private Sound crash;
	private Sound trainSound;
	private Sound fortuneChoiceSound;
	private Sound shuntSound;

	public MyShuntGame(Game game, AssetManager help) {
		this.assets = help;
		this.parent = game;
	}

	private boolean makeSoundForTrain = true;
	private boolean makeSoundForFortuneChoice = true;

	private Environment environment;
	private OrthographicCamera cam;
	private ModelBatch modelBatch;

	private Array<ModelInstance> instances = new Array<>();
	private String[] sceneObjects;

	private ModelInstance shuntInstance;

	private boolean loading;
	private boolean isRotated;

	private int totalScore;
	private BitmapFont font24;
	private SpriteBatch drawingBatch;
	private ModelInstance fortuneChoice;
	private Vector3 beginning;
	private Vector3 trainBeginning;

	private ModelInstance trainInstance;

	private float limit;
	private float trainLimit;
	private float trainSpeed;

	private float transportSpeed;
	private boolean changeScore;

	private btDispatcher dispatcher;
    private btBoxShape gdxCarShape;
	private btCollisionObject gdxCarCollisionObject;
	private btBoxShape gdxTrolleyBusShape;
	private btCollisionObject gdxTrolleyBusCollisionObject;
	private btCollisionObject gdxTrainCollisionObject;
	private btBoxShape gdxTrainShape;
	private btCollisionObject currentCollisionObject;

	private boolean collision;

	private Texture loadingTexture;
	private float scaleCoeffwidth;

	@Override
	public void show() {
		int width = 2130;
		scaleCoeffwidth = (float) width / Gdx.graphics.getWidth();
		sceneObjects = new String[]{"Police", "Car", "Car1", "Car2", "Car3", "Ambulance", "TrolleyBus", "TrolleyBus1", "Train", "Shunt", "Stones", "Stick", "Bus-stop", "Road", "Rails", "Grass", "Home", "Lantern", "Fir-tree"};
		Bullet.init();
		loadingTexture = new Texture(Gdx.files.internal("loading.png"));

		btCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		collision = false;

		gdxCarShape = new btBoxShape(new Vector3(1.4f, 1f, 2.4f));
		gdxCarCollisionObject = new btCollisionObject();
		gdxCarCollisionObject.setCollisionShape(gdxCarShape);

		gdxTrolleyBusShape = new btBoxShape(new Vector3(3.2f, 1f, 2.4f));
		gdxTrolleyBusCollisionObject = new btCollisionObject();
		gdxTrolleyBusCollisionObject.setCollisionShape(gdxTrolleyBusShape);

		gdxTrainShape = new btBoxShape(new Vector3(1f, 1f, 8.8f));
		gdxTrainCollisionObject = new btCollisionObject();
		gdxTrainCollisionObject.setCollisionShape(gdxTrainShape);

		totalScore = 0;
		transportSpeed = -0.1f;
		changeScore = true;

		drawingBatch = new SpriteBatch();

		isRotated = true;
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


		cam = new OrthographicCamera(Gdx.graphics.getWidth()/(100f / scaleCoeffwidth), Gdx.graphics.getHeight()/(100f / scaleCoeffwidth));
		cam.position.set(1f, 1f, 1f);
		cam.lookAt(0,0,0);
		cam.near = -12f;
		cam.far = 350f;
		cam.update();

		scoreMaker();

		loading = true;
	}

	private void scoreMaker() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Arkhip-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)(60 / scaleCoeffwidth);
		parameter.borderWidth = 1;
		parameter.color = Color.BLACK;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.shadowColor = new Color(0.0f, 0.0f, 0.0f, 0.4f);
		font24 = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render(float delta) {
		if (loading) {
			drawingBatch.begin();
			drawingBatch.draw(loadingTexture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			drawingBatch.end();
		}
		instancesLoader();
		if (!loading) {
            collision = checkCollision();
            if (collision) {
            	forest.stop();
				fortuneChoiceSound.stop();
            	trainSound.stop();
				crash.play(0.8f);
				trainSpeed = 0;
				transportSpeed = 0;
				parent.setScreen(new Menu(parent, assets, totalScore));
			}

			if ((fortuneChoice == instances.get(6)) || (fortuneChoice == instances.get(7)))  {
				Matrix4 mat = fortuneChoice.transform.cpy();
				currentCollisionObject.setWorldTransform(mat.translate(1.2f, 0f, 0f));
			} else if (fortuneChoice == instances.get(0) || fortuneChoice == instances.get(1) || fortuneChoice == instances.get(2) ||
					fortuneChoice == instances.get(3) || fortuneChoice == instances.get(4) || fortuneChoice == instances.get(5)) {
				Matrix4 mat = fortuneChoice.transform.cpy();
				currentCollisionObject.setWorldTransform(mat.translate(-0.2f, 0f, 0f));
			}
			if ((isRotated) || (fortuneChoice.transform.getTranslation(new Vector3()).x < 0.28f) || (fortuneChoice.transform.getTranslation(new Vector3()).x > 0.31f)) {
				moveRandomInstance(fortuneChoice);
			}

			if ((trainInstance.transform.getTranslation(new Vector3()).y > 0.4f) && (trainInstance.transform.getTranslation(new Vector3()).y < 2.4)) {
				if (makeSoundForTrain) {
					trainSound.play(0.15f);
					makeSoundForTrain = false;
				}
			} else {
				trainSound.stop();
				makeSoundForTrain = true;
			}

			if ((fortuneChoice.transform.getTranslation(new Vector3()).x > 0.25f) && (fortuneChoice.transform.getTranslation(new Vector3()).x < 0.32)) {
				if (makeSoundForFortuneChoice) {
					fortuneChoiceSound.play(0.7f);
					makeSoundForFortuneChoice = false;
				}
			} else {
				fortuneChoiceSound.stop();
				makeSoundForFortuneChoice = true;
			}

			Matrix4 trainMat = trainInstance.transform.cpy();
			gdxTrainCollisionObject.setWorldTransform(trainMat.translate(0, 0 , -8.8f));
			moveTrain(trainInstance);

			speedIncreaser();
			scoreIncreaser();
			trainCycler();
			instancesCycler();
			serviceFunction();
		}
	}

	private void instancesCycler() {
		if (fortuneChoice.transform.getTranslation(new Vector3()).x < limit) {
			fortuneChoice.transform.setTranslation(beginning);
			fortuneChoice = randomInstance();
			collisionObjectMaker();
			currentCollisionObject.setWorldTransform(fortuneChoice.transform);
			limit = limitGenerator();
			changeScore = true;
		}
	}

	private void instancesLoader() {
		if (loading && assets.update()) {
			for (String element: sceneObjects) {
				ModelInstance modelInstance = new ModelInstance(assets.get(element + ".obj", Model.class));
				boolean b = (element.equals("TrolleyBus")) || (element.equals("TrolleyBus1"))|| (element.equals("Car")) ||
						(element.equals("Car1")) || (element.equals("Car2")) || (element.equals("Car3")) || (element.equals("Ambulance") || (element.equals("Police")));
				if (b) {
					modelInstance.transform.translate(0, 0, -5);
				} else if (element.equals("Shunt")) {
					modelInstance.transform.translate(0f, 0f, 1.3f);
					shuntInstance = modelInstance;
				} else if (element.equals("Train")) {
					trainInstance = modelInstance;
					modelInstance.transform.setToTranslation(-25.0f, 0f, 1f);
					gdxTrainCollisionObject.setWorldTransform(modelInstance.transform);
				} else if (element.equals("Lantern")) {
					modelInstance.transform.translate(0f, 0f, 1.5f);
				} else if (element.equals("Home")) {
					modelInstance.transform.translate(0f, 0f, 0.15f);
				}
				modelInstance.transform.rotate(-3, 75, 0, 90);
				if (b) {
					modelInstance.transform.translate(14, 0, 0.3f);
				} else if (element.equals("Train")) {
					trainBeginning = trainInstance.transform.getTranslation(new Vector3());
				}
				instances.add(modelInstance);
			}

			fortuneChoice = randomInstance();
			collisionObjectMaker();
			currentCollisionObject.setWorldTransform(fortuneChoice.transform);
			limit = limitGenerator();
			trainLimit = trainLimitGenerator();
			trainSpeed = trainSpeedGenerator();
			crash = assets.get("crash.mp3");
			trainSound = assets.get("train.mp3");
			fortuneChoiceSound = assets.get("wheels.mp3");
			shuntSound = assets.get("shuntSound.mp3");
			forest = assets.get("forest.mp3");
			forest.setVolume(0.8f);
			forest.setLooping(true);
			forest.play();
			loading = false;

			Gdx.input.setInputProcessor(new InputAdapter(){
				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button) {
					if(isRotated) {
						shuntSound.play(1f);
						fortuneChoiceSound.stop();
						shuntInstance.transform.rotate(0, 1, 0, 90);
						shuntInstance.transform.translate(2.4f,0,0.217f);
						isRotated = false;
					} else {
						shuntSound.play(1f);
						fortuneChoiceSound.play(0.7f);
						shuntInstance.transform.rotate(0, -1, 0, 90);
						shuntInstance.transform.translate(-0.217f,0,2.4f);
						isRotated = true;
					}
					return true;
				}
			});
		}
	}

	private void speedIncreaser() {
		if (totalScore <= 10) {
			transportSpeed = -0.3f;
		} else if ((totalScore > 10) && (totalScore <= 15)) {
			transportSpeed = -0.4f;
		} else if ((totalScore > 15) && (totalScore <= 20)) {
			transportSpeed = -0.5f;
		} else if ((totalScore > 20) && (totalScore <= 25)) {
			transportSpeed = -0.6f;
		} else {
			transportSpeed = -0.8f;
		}
	}

	private void scoreIncreaser() {
		if ((fortuneChoice.transform.getTranslation(new Vector3()).x < 0.25) && (changeScore)) {
			totalScore++;
			changeScore = false;
		}
	}

	private void trainCycler () {
		if (trainInstance.transform.getTranslation(new Vector3()).y > trainLimit) {
			trainSpeed = trainSpeedGenerator();
			trainInstance.transform.setTranslation(trainBeginning);
			trainLimit = trainLimitGenerator();
		}
	}

	private void serviceFunction() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		drawingBatch.begin();
		font24.draw(drawingBatch, String.valueOf(totalScore), Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 10f);
		drawingBatch.end();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}

	private boolean checkCollision(){
		CollisionObjectWrapper co0 = new CollisionObjectWrapper(currentCollisionObject);
		CollisionObjectWrapper co2 = new CollisionObjectWrapper(gdxTrainCollisionObject);
		btPersistentManifold mfTr = new btPersistentManifold();
		btCollisionAlgorithmConstructionInfo ciTr = new btCollisionAlgorithmConstructionInfo();
		ciTr.setDispatcher1(dispatcher);
		btDispatcherInfo infoTr = new btDispatcherInfo();
		btCollisionAlgorithm trainAlgorithm = new btBoxBoxCollisionAlgorithm(mfTr, ciTr, co0.wrapper, co2.wrapper);
		btManifoldResult trainResult = new btManifoldResult(co0.wrapper, co2.wrapper);
		trainAlgorithm.processCollision(co0.wrapper, co2.wrapper, infoTr, trainResult);
		boolean trainR = trainResult.getPersistentManifold().getNumContacts() > 0;
		trainResult.dispose();
		infoTr.dispose();
		trainAlgorithm.dispose();
		ciTr.dispose();
		co0.dispose();
		co2.dispose();
		return trainR;
	}

	private ModelInstance randomInstance() {
		int random = new Random().nextInt(8);
		ModelInstance fortuneChoice = instances.get(random);
		beginning = fortuneChoice.transform.getTranslation(new Vector3());
		return fortuneChoice;
	}

	private void moveRandomInstance(ModelInstance randomInstance) {
		randomInstance.transform.translate(transportSpeed, 0, 0);
	}

	private void moveTrain(ModelInstance randomInstance) {
		randomInstance.transform.translate(0, 0, trainSpeed);
	}

	private float limitGenerator() {
		return new Random().nextFloat()*(-0.05f) + 0.25f;
	}

	private float trainLimitGenerator() {
		return new Random().nextFloat()*(0.1f) + 2.5f;
	}

	private float trainSpeedGenerator() {
		return new Random().nextFloat()*(0.4f) + 0.1f;
	}

	private void collisionObjectMaker() {
		if ((fortuneChoice == instances.get(0)) || (fortuneChoice == instances.get(1)) || (fortuneChoice == instances.get(2)) ||
				(fortuneChoice == instances.get(3)) || (fortuneChoice == instances.get(4)) || (fortuneChoice == instances.get(5))) {
			currentCollisionObject = gdxCarCollisionObject;
		} else if ((fortuneChoice == instances.get(6)) || (fortuneChoice == instances.get(7))){
			currentCollisionObject = gdxTrolleyBusCollisionObject;
		}
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
		dispatcher.dispose();
		gdxCarShape.dispose();
		gdxTrolleyBusShape.dispose();
		gdxTrainShape.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide(){
	}
}
