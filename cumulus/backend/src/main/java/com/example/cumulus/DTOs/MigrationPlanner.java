package com.example.cumulus.DTOs;

import java.util.List;


public class MigrationPlanner {
 private int migrationTime;
 private String migrationDifficulty;
 private List<String> steps;
 private List<List<String>> keyResources;

 public int getMigrationTime() {
  return migrationTime;
 }

 public void setMigrationTime(int migrationTime) {
  this.migrationTime = migrationTime;
 }

 public String getMigrationDifficulty() {
  return migrationDifficulty;
 }

 public void setMigrationDifficulty(String migrationDifficulty) {
  this.migrationDifficulty = migrationDifficulty;
 }

 public List<String> getSteps() {
  return steps;
 }

 public void setSteps(List<String> steps) {
  this.steps = steps;
 }

 public List<List<String>> getKeyResources() {
  return keyResources;
 }

 public void setKeyResources(List<List<String>> keyResources) {
  this.keyResources = keyResources;
 }
}
