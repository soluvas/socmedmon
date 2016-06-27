var gulp = require('gulp');
var gutil = require('gulp-util');
var bower = require('bower');
//var concat = require('gulp-concat');
// disable sass due to gulp-sass requiring node-gyp requiring gyp requiring python < 3.0
//var sass = require('gulp-sass');
//var minifyCss = require('gulp-minify-css');
//var rename = require('gulp-rename');
//var sh = require('shelljs');
var sourcemaps = require('gulp-sourcemaps');
var ts = require('gulp-typescript');
var webserver = require('gulp-webserver');

var paths = {
  sass: ['./scss/**/*.scss'],
  src: ['./src/*.ts']
};

//gulp.task('default', ['sass']);
gulp.task('default', ['compile']);

gulp.task('compile', function() {
    gulp.src(paths.src)
        .pipe(sourcemaps.init())
        .pipe(ts({sortOutput: true}))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('www/js'));
});

gulp.task('watch', function() {
//  gulp.watch(paths.sass, ['sass']);
  gulp.watch(paths.src, ['compile']);
});

gulp.task('install', [], function() {
  return bower.commands.install()
    .on('log', function(data) {
      gutil.log('bower', gutil.colors.cyan(data.id), data.message);
    });
});

gulp.task('serve', ['compile', 'watch'], () => {
  gulp.src('www')
    .pipe(webserver({
      livereload: true,
      directoryListing: {
        path: 'www'
      },
      open: true
    }));
});
