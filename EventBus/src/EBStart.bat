%ECHO OFF
START "EVENT BUS REGISTRY" /MIN /NORMAL rmiregistry
START "EVENT BUS" /MIN /NORMAL java Framework.EventManager
