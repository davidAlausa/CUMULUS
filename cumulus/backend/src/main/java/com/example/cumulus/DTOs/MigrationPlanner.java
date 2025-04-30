package com.example.cumulus.DTOs;

import com.example.cumulus.Entities.Resource;

import java.util.List;


public class MigrationPlanner {
 private int migrationTime;
 private String migrationDifficulty;
 private List<Resource> keyResources;

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

 public List<Resource> getKeyResources() {
  return keyResources;
 }

 public void setKeyResources(List<Resource> keyResources) {
  this.keyResources = keyResources;
 }
}
