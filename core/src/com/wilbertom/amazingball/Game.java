package com.wilbertom.amazingball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.JsonReader;

public class Game extends ApplicationAdapter {
	private Environment environment;
	private PerspectiveCamera camera;
	private CameraInputController cameraController;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;

	@Override
	public void create () {
		createLitEnvironment();
		createCamera();
		configureController();

		modelBatch = new ModelBatch();

		model = loadModel("AmazingBall.g3dj");

		instance = new ModelInstance(model);
	}

	private Model loadModel(String assetName) {
		return new G3dModelLoader(new JsonReader())
				.loadModel(Gdx.files.internal(assetName));
	}

	private void createLitEnvironment() {
		environment = new Environment();

		environment.set(new ColorAttribute(
				ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f
		));
		environment.add(new DirectionalLight().set(
				0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.02f
		));
	}

	private void createCamera() {
		camera = new PerspectiveCamera(
				67,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()
		);
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
	}

	private void configureController() {
		cameraController = new CameraInputController(camera);
		Gdx.input.setInputProcessor(cameraController);
	}

	@Override
	public void render () {
		camera.update();

		// clear left over from last render cycle
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
	}
}
