/*
 * Copyright 2017 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chenupt.adaptergen.compiler;

import com.chenupt.adaptergen.annotation.Adapter;
import com.google.auto.service.AutoService;
import com.google.common.collect.ArrayListMultimap;
import com.google.googlejavaformat.java.filer.FormattingFiler;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.ClassUtils;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by chenupt on 2017/1/23.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.chenupt.adaptergen.annotation.Adapter"})
public class Adapterprocessor extends AbstractProcessor {

	private ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
		for (TypeElement annotation : annotations) {
			Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
			for (Element element : elements) {
				if (element instanceof TypeElement) {
					Adapter adapterAnnotation = element.getAnnotation(Adapter.class);
					multimap.put(adapterAnnotation.name(), ((TypeElement) element).getQualifiedName().toString());
				}
			}
		}

		for (String adapterName : multimap.keySet()) {
			try {
				generateAdapter(adapterName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	private void generateAdapter(String adapterName) throws Exception {

		List<String> items = multimap.get(adapterName);

		MethodSpec construction = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addStatement("setHasStableIds(true)")
				.build();

		MethodSpec.Builder onCreateViewHolderBuilder = MethodSpec.methodBuilder("onCreateViewHolder")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(ClassName.get("android.view", "ViewGroup"), "parent")
				.addParameter(TypeName.INT, "viewType")
				.returns(ClassName.get("android.support.v7.widget", "RecyclerView", "ViewHolder"));

		for (int i = 0; i < items.size(); i++) {
			String itemName = items.get(i);
			if (i == 0) {
				onCreateViewHolderBuilder.beginControlFlow("if (viewType == $L)", i);
			} else {
				onCreateViewHolderBuilder.beginControlFlow("else if (viewType == $L)", i);
			}
			onCreateViewHolderBuilder.addStatement("$T item = new $T()",
					ClassName.get(getPackageName(itemName), getClassName(itemName)), ClassName.get(getPackageName(itemName), getClassName(itemName)));
			onCreateViewHolderBuilder.addStatement("item.createView(parent)");
			onCreateViewHolderBuilder.addStatement("return new $T<>(item)", ClassName.get("com.chenupt.adaptergen.core", "ViewHolderWrapper"));
			onCreateViewHolderBuilder.endControlFlow();
		}
		onCreateViewHolderBuilder.addStatement("return null");


		MethodSpec onCreateViewHolder = onCreateViewHolderBuilder.build();

		MethodSpec onBindViewHolder = MethodSpec.methodBuilder("onBindViewHolder")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(ClassName.get("android.support.v7.widget", "RecyclerView", "ViewHolder"), "holder")
				.addParameter(TypeName.INT, "position")
				.addStatement("(($T) holder).bindData(dataList.get(position))", ClassName.get("com.chenupt.adaptergen.core", "ViewHolderWrapper"))
				.build();

		MethodSpec getItemCount = MethodSpec.methodBuilder("getItemCount")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addStatement("return dataList.size()")
				.returns(TypeName.INT)
				.build();

		MethodSpec.Builder builder = MethodSpec.methodBuilder("getItemViewType")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(TypeName.INT, "position")
				.returns(TypeName.INT);

		for (int i = 0; i < items.size(); i++) {
			String itemName = items.get(i);
			if (i == 0) {
				builder.beginControlFlow("if (dataList.get(position).vh == $T.class)", ClassName.get(getPackageName(itemName), getClassName(itemName)));
			} else {
				builder.beginControlFlow("else if (dataList.get(position).vh == $T.class)", ClassName.get(getPackageName(itemName), getClassName(itemName)));
			}
			builder.addStatement("return $L", i)
					.endControlFlow();
		}
		builder.addStatement("return super.getItemViewType(position)");

		MethodSpec getItemViewType = builder.build();

		MethodSpec getItemId = MethodSpec.methodBuilder("getItemId")
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addParameter(TypeName.INT, "position")
				.addStatement("return dataList.get(position).hashCode()")
				.returns(TypeName.LONG)
				.build();

		FieldSpec fieldSpec = FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get("com.chenupt.adaptergen.core", "ItemData")), "dataList")
				.addModifiers(Modifier.PUBLIC)
				.initializer("new $T<>()", ClassName.get("java.util", "ArrayList"))
				.build();

		TypeSpec typeSpec = TypeSpec.classBuilder(adapterName)
				.addModifiers(Modifier.PUBLIC)
				.addField(fieldSpec)
				.superclass(ClassName.get("android.support.v7.widget", "RecyclerView", "Adapter"))
				.addMethod(construction)
				.addMethod(onCreateViewHolder)
				.addMethod(onBindViewHolder)
				.addMethod(getItemCount)
				.addMethod(getItemViewType)
				.addMethod(getItemId)
				.build();

		JavaFile javaFile = JavaFile.builder("com.chenupt.adaptergen", typeSpec)
				.build();
		Filer filer = new FormattingFiler(processingEnv.getFiler());
		try {
			javaFile.writeTo(filer);
		} catch (FilerException e) {
			// ignore
		}
	}

	private String getPackageName(String item) {
		return ClassUtils.getPackageName(item);
	}

	private String getClassName(String item) {
		return ClassUtils.getShortClassName(item);
	}

}
