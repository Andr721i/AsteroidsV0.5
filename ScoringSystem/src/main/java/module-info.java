module dk.sdu.mmmi.cbse.scoresystem {
    requires dk.sdu.mmmi.cbse.scoreserviceclient;
    requires Common;
    requires CommonAsteroids;
    requires CommonEnemy;      // Require the common module for shared classes/interfaces
    exports dk.sdu.mmmi.cbse.scoresystem; // Export your package so it can be accessed by others
    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.scoresystem.ScoreSystem;
}
